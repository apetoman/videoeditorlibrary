package com.eju.cy.videoeditor

/**
 *@author: cc
 *@description:存放一些常量
 **/
class AppTags {
    companion object {

        const val PHONE = "phone"//手机号
        const val PHONE_CODE = "phone_code"//国家编号
        const val COUNTRY_NAME = "country_name"//国家名字
        const val REQUEST_CODE_CHOOSE: Int = 2019// 设置作为标记的请求码
        const val POSITON = "positon"//下标
        const val ORDER_NO = "orderNo"//订单编号
        const val BIKE_NO = "bikeNo"//滑板车编号
        const val CARD_NO = "card_no"//银行卡编号
        const val INTENT_PARAM_AUTH = "intent://param_auth"//是否认证
        const val BOUND_PHONE_NUMBER = "bound_phone_number"//绑定手机号标识
        const val ITINERARY_AMOUNT = "itinerary_amount"//价格
        const val ITINERARYNO = "itineraryno"//我的行程ID

        //蓝牙
        const val BLE_UUID_UART_SERVICE = "69400001-b5a3-f393-e0a9-e50e24dcca99"
        const val BLE_UUID_UART_WHITE_CHARACTERISTIC = "69400002-b5a3-f393-e0a9-e50e24dcca99"
        const val BLE_UUID_UART_READ_CHARACTERISTIC = "69400003-b5a3-f393-e0a9-e50e24dcca99"
        const val SEND_ITINERARY_NO = "itineraryNos"
        //消息
        const val MSG_TITLE = "msg_title"
        const val MSG_CONTENT = "msg_content"

    }

}