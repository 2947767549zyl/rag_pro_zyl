import request from '@/utils/request'

// 查询停车费率配置列表
export function listRate(query) {
  return request({
    url: '/system/rate/park/list',
    method: 'get',
    params: query
  })
}

// 查询停车费率配置详细
export function getRate(id) {
  return request({
    url: '/system/rate/park/' + id,
    method: 'get'
  })
}

// 新增停车费率配置
export function addRate(data) {
  return request({
    url: '/system/rate/park',
    method: 'post',
    data: data
  })
}

// 修改停车费率配置
export function updateRate(data) {
  return request({
    url: '/system/rate/park',
    method: 'put',
    data: data
  })
}

// 删除停车费率配置
export function delRate(id) {
  return request({
    url: '/system/rate/park/' + id,
    method: 'delete'
  })
}
