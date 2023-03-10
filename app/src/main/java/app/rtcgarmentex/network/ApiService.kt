package app.rtcgarmentex.network

import app.rtcgarmentex.data.request.LoginRequest
import app.rtcgarmentex.data.request.PasswordRequest
import app.rtcgarmentex.data.request.ProfileRequest
import app.rtcgarmentex.data.response.*
import app.rtcgarmentex.data.response.receivingResponse.ReceivingDetailResponse
import app.rtcgarmentex.utils.ConstantsHelper.GET_CUSTOMER_LIST
import app.rtcgarmentex.utils.ConstantsHelper.GET_ORDER_LIST_PAGE
import app.rtcgarmentex.utils.ConstantsHelper.GET_PARTICULAR_LIST
import app.rtcgarmentex.utils.ConstantsHelper.GET_RECEIVING_DETAILS
import app.rtcgarmentex.utils.ConstantsHelper.GET_RECEIVING_LIST_PAGE
import app.rtcgarmentex.utils.ConstantsHelper.GET_RECEIVING_SEARCH_LIST
import app.rtcgarmentex.utils.ConstantsHelper.GET_SUPPLIER_LIST
import app.rtcgarmentex.utils.ConstantsHelper.GET_TRANSPORT_LIST
import app.rtcgarmentex.utils.ConstantsHelper.POST_ADD_ORDER
import app.rtcgarmentex.utils.ConstantsHelper.POST_ADD_RECEIVING
import app.rtcgarmentex.utils.ConstantsHelper.POST_LOGIN
import app.rtcgarmentex.utils.ConstantsHelper.POST_UPDATE_PASSWORD
import app.rtcgarmentex.utils.ConstantsHelper.POST_UPDATE_PROFILE
import app.rtcgarmentex.utils.ConstantsHelper.SEARCH_ORDER_LIST
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.ArrayList

interface
ApiService {

    @POST(POST_LOGIN)
    fun requestLogin(@Body requestOtpRequest: LoginRequest): Call<LoginResponse>

    @POST(POST_UPDATE_PROFILE)
    fun updateProfile(@Body request: ProfileRequest): Call<BaseResponseModel>

    @POST(POST_UPDATE_PASSWORD)
    fun updatePassword(@Body request: PasswordRequest): Call<BaseResponseModel>

    @POST(POST_ADD_ORDER)
    fun postAddOrder(@Body body: MultipartBody): Call<BaseResponseModel>

    @GET(GET_ORDER_LIST_PAGE)
    fun getOrderList(@Query("emp_id") emp_id: Int, @Query("page") page: Int): Call<OrderListResponse>

    @GET(SEARCH_ORDER_LIST)
    fun searchOrderList(@Query("emp_id") emp_id: Int, @Query("search") search: String): Call<OrderListResponse>

    @GET(GET_SUPPLIER_LIST)
    fun getSupplierList(): Call<ArrayList<SupplierResponse>>

    @GET(GET_CUSTOMER_LIST)
    fun getCustomerList(): Call<ArrayList<CustomerResponse>>

    @GET(GET_PARTICULAR_LIST)
    fun getParticularList(): Call<ArrayList<StringResponse>>

    @GET(GET_TRANSPORT_LIST)
    fun getTransportList(): Call<ArrayList<StringResponse>>

    @GET(GET_RECEIVING_LIST_PAGE)
    fun getReceivingList(@Query("emp_id") emp_id: Int, @Query("page") page: Int): Call<ReceivingListResponse>

    @GET(GET_RECEIVING_SEARCH_LIST)
    fun getReceivingSearchList(@Query("emp_id") emp_id: Int): Call<ReceivingOrdersSearchListResponse>

    @GET(GET_RECEIVING_DETAILS)
    fun getReceivingDetails(@Query("emp_id") emp_id: Int, @Query("order_no") orderNo: String): Call<ReceivingDetailResponse>

    @POST(POST_ADD_RECEIVING)
    fun postAddReceiving(@Body body: MultipartBody): Call<BaseResponseModel>

}
