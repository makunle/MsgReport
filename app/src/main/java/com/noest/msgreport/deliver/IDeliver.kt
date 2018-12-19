package com.noest.msgreport.deliver

interface IDeliver {
    fun deliver(msg: String, from: String): Boolean
}