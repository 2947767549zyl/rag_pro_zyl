package com.thinglinks.component.tcp;

import com.thinglinks.common.utils.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户端连接处理器（支持自定义分隔符）
 */
public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final String clientId;
    private final ServerHandler handler;
    private PrintWriter out;
    private BufferedReader in;
    private volatile boolean isConnected = true;
    private String componentId;
    private String delimiter;
    private String[] delimiters;
    private boolean USE_CUSTOM_PARSER = false;

    public ClientHandler(String componentId, Socket socket, String clientId,
                         ServerHandler handler, String delimiter) {
        this.clientSocket = socket;
        this.clientId = clientId;
        this.handler = handler;
        this.componentId = componentId;
        this.delimiter = delimiter;

        // 处理分隔符配置
        if (StringUtils.isNotEmpty(this.delimiter)) {
            delimiters = this.delimiter.split(",");
            // 转义特殊字符
            for (int i = 0; i < delimiters.length; i++) {
                delimiters[i] = unescapeDelimiter(delimiters[i].trim());
            }
            this.USE_CUSTOM_PARSER = true;
        }
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));

            // 通知处理器有新连接
            handler.onClientConnected(clientId);

            // 根据分隔符配置选择处理方式
            if (USE_CUSTOM_PARSER) {
                // 使用自定义分隔符解析器
                processWithCustomDelimiter();
            } else {
                // 使用默认的readLine方式（兼容原逻辑）
                processWithReadLine();
            }

        } catch (IOException e) {
            if (isConnected) {
                System.err.println("客户端处理异常: " + clientId + ", 错误: " + e.getMessage());
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            close();
            TCPServerInstance server = TCPServerManager.getServerInstance(componentId);
            if (server != null) {
                server.removeClient(clientId);
            }

            handler.onClientDisconnected(clientId);
        }
    }

    /**
     * 使用readLine方式处理（默认）
     */
    private void processWithReadLine() throws IOException, InvocationTargetException, IllegalAccessException {
        String inputLine;
        while (isConnected && (inputLine = in.readLine()) != null) {
            handler.onMessageReceived(componentId, clientId, inputLine);
        }
    }

    /**
     * 使用自定义分隔符解析器
     */
    private void processWithCustomDelimiter() throws IOException, InvocationTargetException, IllegalAccessException {
        StringBuilder buffer = new StringBuilder();
        char[] charBuffer = new char[4096];

        while (isConnected) {
            int charsRead;
            try {
                charsRead = in.read(charBuffer);
                if (charsRead == -1) {
                    // 连接已关闭
                    break;
                }

                // 将读取的数据添加到缓冲区
                buffer.append(charBuffer, 0, charsRead);

                // 处理缓冲区中的完整消息
                processBuffer(buffer);

            } catch (IOException e) {
                if (isConnected) {
                    throw e;
                }
                break;
            }
        }

        // 处理缓冲区中剩余的数据
        if (buffer.length() > 0) {
            handler.onMessageReceived(componentId, clientId, buffer.toString());
        }
    }

    /**
     * 处理缓冲区，根据分隔符分割消息
     */
    private void processBuffer(StringBuilder buffer)
            throws InvocationTargetException, IllegalAccessException {
        String content = buffer.toString();
        List<String> messages = new ArrayList<>();
        int lastEnd = 0;

        while (true) {
            int delimiterIndex = -1;
            String foundDelimiter = null;

            // 查找第一个出现的分隔符
            for (String delim : delimiters) {
                int index = content.indexOf(delim, lastEnd);
                if (index != -1 && (delimiterIndex == -1 || index < delimiterIndex)) {
                    delimiterIndex = index;
                    foundDelimiter = delim;
                }
            }

            if (delimiterIndex == -1) {
                // 没有找到更多分隔符
                break;
            }

            // 提取消息
            String message = content.substring(lastEnd, delimiterIndex);
            messages.add(message);

            // 更新位置，跳过分隔符
            lastEnd = delimiterIndex + foundDelimiter.length();
        }

        // 处理所有找到的完整消息
        for (String message : messages) {
            if (StringUtils.isNotEmpty(message)) {
                handler.onMessageReceived(componentId, clientId, message);
            }
        }

        // 保留未处理的数据
        if (lastEnd < content.length()) {
            buffer.setLength(0);
            buffer.append(content.substring(lastEnd));
        } else {
            buffer.setLength(0);
        }
    }

    /**
     * 转义分隔符字符串
     * 支持：\n, \r, \t, \\, \0, \r\n
     */
    private String unescapeDelimiter(String delimiter) {
        if (delimiter == null) {
            return "";
        }

        // 如果是空字符串，直接返回
        if (delimiter.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        int length = delimiter.length();

        for (int i = 0; i < length; i++) {
            char c = delimiter.charAt(i);

            if (c == '\\' && i + 1 < length) {
                char next = delimiter.charAt(i + 1);
                switch (next) {
                    case 'n':
                        result.append('\n');
                        i++;
                        break;
                    case 'r':
                        // 检查是否是 \r\n
                        if (i + 3 < length && delimiter.charAt(i + 2) == '\\' && delimiter.charAt(i + 3) == 'n') {
                            result.append("\r\n");
                            i += 3;
                        } else {
                            result.append('\r');
                            i++;
                        }
                        break;
                    case 't':
                        result.append('\t');
                        i++;
                        break;
                    case '0':
                        result.append('\0');
                        i++;
                        break;
                    case '\\':
                        result.append('\\');
                        i++;
                        break;
                    default:
                        // 如果不是已知的转义序列，保持原样（包括反斜杠）
                        result.append(c).append(next);
                        i++;
                        break;
                }
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    /**
     * 发送消息到客户端
     */
    public void sendMessage(String message) {
        if (out != null && isConnected) {
            out.println(message);
            out.flush();
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        isConnected = false;
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            System.err.println("关闭客户端连接时出错: " + clientId);
        }
    }

    public String getClientId() {
        return clientId;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String[] getDelimiters() {
        return delimiters != null ? delimiters.clone() : new String[0];
    }
}