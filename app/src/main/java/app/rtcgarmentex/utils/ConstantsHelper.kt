package app.rtcgarmentex.utils

object ConstantsHelper {
//    const val BASE_URL = "http://aviationndts.com/rtc/public/api/"
    const val BASE_URL = "http://www.rtcgarmentex.com/app/public/api/"

    const val POST_LOGIN = "login"
    const val POST_UPDATE_PROFILE = "update-user-profile"
    const val POST_UPDATE_PASSWORD = "update-user-password"
    const val POST_ADD_ORDER = "employee-order-store"
    const val GET_ORDER_LIST = "employee-order-list"
    const val GET_ORDER_LIST_PAGE = "employee-order-list-page"//?emp_id=4&page=1
    const val SEARCH_ORDER_LIST = "search-order"
    const val GET_SUPPLIER_LIST = "get-all-suppliers"
    const val GET_CUSTOMER_LIST = "get-all-customers"
    const val GET_PARTICULAR_LIST = "get_all_goods"
    const val GET_TRANSPORT_LIST = "get_all_transport"
    const val GET_RECEIVING_LIST_PAGE = "get_employee_receiving"//?emp_id=4&page=1
    const val GET_RECEIVING_SEARCH_LIST = "employee-order-list"//?emp_id=4
    const val GET_RECEIVING_DETAILS = "get_single_receiving"//?emp_id=4&order_no=DLO-1181
    const val POST_ADD_RECEIVING = "update_receiving"

    const val MIN_PASSWORD_LENGTH = 6
    const val PHONE_NUMBER_LENGTH = 10
}