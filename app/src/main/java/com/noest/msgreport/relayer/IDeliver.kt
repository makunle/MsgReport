package com.noest.msgreport.relayer

interface IDeliver {
    fun deliver(msg: String, from: String): Boolean
}