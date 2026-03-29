import request from '@/utils/request'

// 查询电子围栏列表
export function listGeofence(query) {
    return request({
        url: '/system/geofence/list',
        method: 'get',
        params: query
    })
}

// 查询电子围栏详细
export function getGeofence(id) {
    return request({
        url: '/system/geofence/' + id,
        method: 'get'
    })
}

// 新增电子围栏
export function addGeofence(data) {
    return request({
        url: '/system/geofence',
        method: 'post',
        data: data
    })
}

// 修改电子围栏
export function updateGeofence(data) {
    return request({
        url: '/system/geofence',
        method: 'put',
        data: data
    })
}

// 删除电子围栏
export function delGeofence(id) {
    return request({
        url: '/system/geofence/' + id,
        method: 'delete'
    })
}
