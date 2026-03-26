import request from '@/utils/request'

// 查询RUC费率配置列表
export function listRate(query) {
  return request({
    url: '/system/rate/ruc/list',
    method: 'get',
    params: query
  })
}

// 查询RUC费率配置详细
export function getRate(id) {
  return request({
    url: '/system/rate/ruc/' + id,
    method: 'get'
  })
}

// 新增RUC费率配置
export function addRate(data) {
  return request({
    url: '/system/rate/ruc',
    method: 'post',
    data: data
  })
}

// 修改RUC费率配置
export function updateRate(data) {
  return request({
    url: '/system/rate/ruc',
    method: 'put',
    data: data
  })
}

// 删除RUC费率配置
export function delRate(id) {
  return request({
    url: '/system/rate/ruc/' + id,
    method: 'delete'
  })
}
