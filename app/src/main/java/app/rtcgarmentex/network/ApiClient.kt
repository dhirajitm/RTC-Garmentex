package app.rtcgarmentex.network

import android.util.Log
import app.rtcgarmentex.utils.ConstantsHelper
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.UnsupportedEncodingException
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.URLDecoder
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

class ApiClient {

    companion object {

        private const val TAG = "ApiClient"
        private const val CONNECT_TIMEOUT_MULTIPLIER = 1
        private const val DEFAULT_CONNECT_TIMEOUT_IN_SEC = 60 * CONNECT_TIMEOUT_MULTIPLIER
        private const val DEFAULT_WRITE_TIMEOUT_IN_SEC = 60 * CONNECT_TIMEOUT_MULTIPLIER
        private const val DEFAULT_READ_TIMEOUT_IN_SEC = 60 * CONNECT_TIMEOUT_MULTIPLIER
        private const val NO_OF_LOG_CHAR = 1000

        private var sRetrofitClient: Retrofit? = null
        private val sDispatcher: Dispatcher? = null

        private fun getClient(): Retrofit? {
            if (sRetrofitClient == null) {
                sRetrofitClient = Retrofit.Builder()
                    .baseUrl(ConstantsHelper.BASE_URL)
//                    .baseUrl("$BASE_URL/")
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClientBuilder().build())
                    .build()
            }
            return sRetrofitClient
        }

        fun <T> buildService(service: Class<T>): T {
            return getClient()!!.create(service)
        }

        private fun getOkHttpClientBuilder(): OkHttpClient.Builder {
            /*OkHttp client builder*/
            val oktHttpClientBuilder = OkHttpClient.Builder()
                .connectTimeout((CONNECT_TIMEOUT_MULTIPLIER * DEFAULT_CONNECT_TIMEOUT_IN_SEC).toLong(), TimeUnit.SECONDS)
                .writeTimeout((CONNECT_TIMEOUT_MULTIPLIER * DEFAULT_WRITE_TIMEOUT_IN_SEC).toLong(), TimeUnit.SECONDS)
                .readTimeout((CONNECT_TIMEOUT_MULTIPLIER * DEFAULT_READ_TIMEOUT_IN_SEC).toLong(), TimeUnit.SECONDS)
//                    .cookieJar(JavaNetCookieJar(getCookieManager())) /* Using okhttp3 cookie instead of java net cookie*/
            oktHttpClientBuilder.dispatcher(getDispatcher())

            oktHttpClientBuilder.addInterceptor { chain ->
                val builder = chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
//                    .addHeader("Authorization", "Bearer " + AppSharedPref.getToken(MyApplication.getApplicationContext()))
//                    .addHeader("apiAuthType", API_AUTH_TYPE)
//                    .addHeader("token", AuthKeyHelper.getInstance().token.toString())
                chain.proceed(builder.build())
            }

            oktHttpClientBuilder.addInterceptor(getHttpLoggingInterceptor())
            oktHttpClientBuilder.addInterceptor { chain ->
                val request = chain.request()

                printPostmanFormattedLog(request)

                val response = chain.proceed(request)
                Log.d(TAG, "intercept: " + response.code)
                response
            }
            return oktHttpClientBuilder
        }

        private fun printPostmanFormattedLog(request: Request) {
            try {
                val allParams: String
                allParams = if (request.method == "GET" || request.method == "DELETE") {
                    request.url.toString().substring(request.url.toString().indexOf("?") + 1, request.url.toString().length)
                } else {
                    val buffer = Buffer()
                    request.body!!.writeTo(buffer)
                    buffer.readString(Charset.forName("UTF-8"))
                }
                val params = allParams.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val paramsString = StringBuilder("\n")
                for (param in params) {
                    paramsString.append(decode(param.replace("=", ":")))
                    paramsString.append("\n")
                }
                Log.d(TAG, paramsString.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        private fun getHttpLoggingInterceptor(): Interceptor {
            val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                if (message.length > NO_OF_LOG_CHAR) {
                    for (noOfLogs in 0..message.length / NO_OF_LOG_CHAR) {
                        if (noOfLogs * NO_OF_LOG_CHAR + NO_OF_LOG_CHAR < message.length) {
                            Log.d(TAG, message.substring(noOfLogs * NO_OF_LOG_CHAR, noOfLogs * NO_OF_LOG_CHAR + NO_OF_LOG_CHAR))
                        } else {
                            Log.d(TAG, message.substring(noOfLogs * NO_OF_LOG_CHAR, message.length))
                        }
                    }
                } else {
                    Log.d(TAG, message)
                }
            })
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return loggingInterceptor
        }

        private fun getCookieManager(): CookieManager {
            val cookieManager = CookieManager()
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
            return cookieManager
        }

        private fun getDispatcher(): Dispatcher {
            return sDispatcher ?: Dispatcher()
        }

        private fun decode(url: String): String {
            return try {
                var prevURL = ""
                var decodeURL = url
                while (prevURL != decodeURL) {
                    prevURL = decodeURL
                    decodeURL = URLDecoder.decode(decodeURL, "UTF-8")
                }
                decodeURL
            } catch (e: UnsupportedEncodingException) {
                "Issue while decoding" + e.message
            }
        }

    }

}
