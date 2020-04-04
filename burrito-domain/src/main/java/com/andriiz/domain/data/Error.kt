package com.andriiz.domain.data

import com.andriiz.domain.R

enum class Error (val message: Int) {
    LOCATION_PERMISSION_NOT_GRANTED(R.string.location_permission_not_granted),
    LOCATION_REQUEST_FAILED(R.string.location_request_failed),
    UNKNOWN_ERROR(R.string.unknown_error)
}