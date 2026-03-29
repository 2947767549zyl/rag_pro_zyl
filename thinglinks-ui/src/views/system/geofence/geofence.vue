<template>
  <div class="app-container">
    <!-- 1. 查询表单 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">
      <el-form-item label="围栏名称" prop="fenceName">
        <el-input
          v-model="queryParams.fenceName"
          placeholder="请输入围栏名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="围栏状态" clearable>
          <el-option label="正常" value="0" />
          <el-option label="停用" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 2. 操作按钮 -->
    <!-- 2. 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:geofence:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:geofence:remove']"
        >删除</el-button>
      </el-col>
    </el-row>

    <!-- 3. 数据表格 -->
    <el-table v-loading="loading" :data="geofenceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="围栏名称" align="center" prop="fenceName" />
      <el-table-column label="类型" align="center" prop="fenceType">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.fenceType === '1'">圆形</el-tag>
          <el-tag v-else-if="scope.row.fenceType === '2'" type="success">多边形</el-tag>
          <el-tag v-else-if="scope.row.fenceType === '3'" type="warning">矩形</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:geofence:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:geofence:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 4. 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body @opened="initMap">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="围栏名称" prop="fenceName">
              <el-input v-model="form.fenceName" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="围栏状态">
              <el-radio-group v-model="form.status">
                <el-radio label="0">正常</el-radio>
                <el-radio label="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="围栏类型" prop="fenceType">
              <el-select v-model="form.fenceType" placeholder="请选择" @change="clearMap">
                <el-option label="圆形" value="1" />
                <el-option label="多边形" value="2" />
                <el-option label="矩形" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收费规则" prop="chargeRuleId">
              <el-select v-model="form.chargeRuleId" placeholder="关联收费项目">
                <el-option label="默认停车收费" :value="1" />
                <el-option label="VIP免费区域" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <div class="map-box">
          <div class="map-tools">
            <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleDraw">开始绘制</el-button>
            <el-button type="danger" size="mini" icon="el-icon-delete" @click="clearMap">清除重画</el-button>
            <span class="map-tip"> 提示：点击按钮后，在地图上绘制范围。</span>
          </div>
          <div id="map-container"></div>
        </div>

        <el-form-item label="备注" prop="remark" style="margin-top:15px">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listGeofence, getGeofence, delGeofence, addGeofence, updateGeofence } from "@/api/system/geofence";
import AMapLoader from '@amap/amap-jsapi-loader';

export default {
  name: "Geofence",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 围栏表格数据
      geofenceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        fenceName: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        fenceName: [{ required: true, message: "围栏名称不能为空", trigger: "blur" }],
        fenceType: [{ required: true, message: "请选择围栏类型", trigger: "change" }]
      },
      // 地图实例
      map: null,
      mouseTool: null,
      currentOverlay: null,
      AMap: null
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询围栏列表 */
    getList() {
      this.loading = true;
      listGeofence(this.queryParams).then(response => {
        this.geofenceList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    /** 初始化地图（弹窗打开后调用） */
    initMap() {
      // 如果已经初始化过，且地图容器存在，直接尝试回显
      if (this.map) {
        this.map.destroy(); // 销毁重建，防止容器复用问题
      }

      AMapLoader.load({
        key: "c5738037e81640fefb6233b6791ff367",
        version: "2.0",
        plugins: ['AMap.MouseTool']
      }).then((AMap) => {
        this.AMap = AMap;
        this.map = new AMap.Map("map-container", {
          zoom: 14,
          center: [116.397428, 39.90923]
        });

        this.mouseTool = new AMap.MouseTool(this.map);

        // 监听绘制结束事件
        this.mouseTool.on('draw', (e) => {
          this.currentOverlay = e.obj;
          this.saveCoordinates(e.obj);
          this.mouseTool.close(false); // 关闭绘制模式，但保留覆盖物
        });

        // 如果是修改操作，且已有坐标，则回显
        if (this.form.coordinates) {
          this.drawBack();
        }
      }).catch(e => {
        console.error("地图加载失败", e);
      });
    },

    /** 开始绘制 */
    handleDraw() {
      if (!this.form.fenceType) {
        this.$modal.msgError("请先选择围栏类型");
        return;
      }
      this.clearMap();
      const drawStyle = {
        strokeColor: "#FF33FF",
        strokeWeight: 6,
        strokeOpacity: 0.2,
        fillColor: '#1791fc',
        fillOpacity: 0.4,
        strokeStyle: "solid",
      };

      if (this.form.fenceType === '1') {
        this.mouseTool.circle(drawStyle);
      } else if (this.form.fenceType === '2') {
        this.mouseTool.polygon(drawStyle);
      } else if (this.form.fenceType === '3') {
        this.mouseTool.rectangle(drawStyle);
      }
    },

    /** 将地图对象保存为JSON字符串 */
    saveCoordinates(obj) {
      if (this.form.fenceType === '1') { // 圆形
        const center = obj.getCenter();
        this.form.coordinates = JSON.stringify({
          center: [center.lng, center.lat],
          radius: obj.getRadius()
        });
      } else { // 多边形 (2) 或 矩形 (3)
        const path = obj.getPath().map(p => [p.lng, p.lat]);
        this.form.coordinates = JSON.stringify(path);
      }
    },

    /** 回显已有围栏 */
    drawBack() {
      if (!this.form.coordinates) return;
      const data = JSON.parse(this.form.coordinates);

      if (this.form.fenceType === '1') {
        this.currentOverlay = new this.AMap.Circle({
          center: data.center,
          radius: data.radius,
          strokeColor: "#FF33FF",
          fillOpacity: 0.4,
          map: this.map
        });
      } else {
        this.currentOverlay = new this.AMap.Polygon({
          path: data,
          strokeColor: "#FF33FF",
          fillOpacity: 0.4,
          map: this.map
        });
      }
      this.map.setFitView([this.currentOverlay]);
    },

    /** 清除地图绘制内容 */
    clearMap() {
      if (this.currentOverlay) {
        this.map.remove(this.currentOverlay);
      }
      this.currentOverlay = null;
      this.form.coordinates = undefined;
      if (this.mouseTool) {
        this.mouseTool.close(true); // 关闭并清除未完成的绘制
      }
    },

    /** 搜索 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    /** 重置搜索 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },

    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },

    /** 状态修改 */
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$modal.confirm('确认要' + text + '"' + row.fenceName + '"围栏吗？').then(() => {
        return updateGeofence(row);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(() => {
        row.status = row.status === "0" ? "1" : "0";
      });
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加电子围栏";
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getGeofence(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改电子围栏";
      });
    },

    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (!this.form.coordinates) {
            this.$modal.msgError("请先在地图上绘制围栏范围");
            return;
          }
          if (this.form.id != null) {
            updateGeofence(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addGeofence(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },

    /** 取消按钮 */
    cancel() {
      this.open = false;
      this.reset();
    },

    /** 表单重置 */
    reset() {
      this.form = {
        id: null,
        fenceName: null,
        fenceType: "1",
        status: "0",
        chargeRuleId: 1,
        coordinates: null,
        remark: null
      };
      this.resetForm("form");
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除围栏编号为"' + ids + '"的数据项？').then(() => {
        return delGeofence(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    }
  }
};
</script>

<style scoped>
.map-box {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
  position: relative;
}
.map-tools {
  position: absolute;
  top: 10px;
  left: 10px;
  z-index: 10;
  background: rgba(255, 255, 255, 0.9);
  padding: 10px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
.map-tip {
  font-size: 12px;
  color: #909399;
  margin-left: 10px;
}
#map-container {
  width: 100%;
  height: 450px;
}
</style>
