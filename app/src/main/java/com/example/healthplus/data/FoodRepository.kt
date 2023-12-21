import com.example.healthplus.data.Food
import com.example.healthplus.data.FoodApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response

const val BASE_ENDPOINT_URL ="https://api.api-ninjas.com/v1/"
const val API_KEY = "VIA4++Z6JrjPomS+N/LZHw==3e1GXcAKfgNj5Psd" // Ersätt med din API key

class FoodRepository {
    // Interceptor för att lägga till X-Api-Key header
    private val apiKeyInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val requestWithApiKey = originalRequest.newBuilder()
            .header("X-Api-Key", API_KEY)
            .build()
        chain.proceed(requestWithApiKey)
    }
    // OkHttp Client med interceptor
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(apiKeyInterceptor)
        .build()
    // Retrofit med OkHttp klient
    private val retrofit: Retrofit by lazy {
        val moshi =
            Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        Retrofit.Builder()
            .baseUrl(BASE_ENDPOINT_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
    private val foodApi: FoodApi by lazy {
        retrofit.create(FoodApi::class.java)
    }
     suspend fun getStatements(str: String): Food {
        val response = foodApi.getStatements("$str")
        return if (response.isSuccessful)
            response.body() ?: Food("",0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0)
        else
            Food("",0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0)
    }
}
