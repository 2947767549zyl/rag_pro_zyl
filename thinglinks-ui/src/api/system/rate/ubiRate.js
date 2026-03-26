import request from '@/utils/request'

// 查询UBI基础费率配置列表
export function listRate(query) {
  return request({
    url: '/system/rate/ubi/list',
    method: 'get',
    params: query
  })
}

// 查询UBI基础费率配置详细
export function getRate(id) {
  return request({
    url: '/system/rate/ubi/' + id,
    method: 'get'
  })
}

// 新增UBI基础费率配置
export function addRate(data) {
  return request({
    url: '/system/rate/ubi',
    method: 'post',
    data: data
  })
}

// 修改UBI基础费率配置
export function updateRate(data) {
  return request({
    url: '/system/rate/ubi',
    method: 'put',
    data: data
  })
}

// 删除UBI基础费率配置
export function delRate(id) {
  return request({
    url: '/system/rate/ubi/' + id,
    method: 'delete'
  })
}
