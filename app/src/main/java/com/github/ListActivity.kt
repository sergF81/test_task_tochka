package com.github


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class ListActivity : AppCompatActivity() {

    //создаем массив, в котором будут храниться данные о логине пользователя
    var userArray: MutableList<String> = mutableListOf()

    //создаем массив, в котором будут храниться данные о логине пользователя при недоступности сервера
    var userArrayOffLine: MutableList<String> = mutableListOf()

    //создаем массив, в котором будут храниться данные о ID пользователя
    var userIdArray: MutableList<String> = mutableListOf()

    //создаем массив, в котором будут храниться данные о ID пользователя при недоступности сервера
    var userIdArrayOffLine: MutableList<String> = mutableListOf()

    //создаем массив, в котором будут храниться данные о аватаре пользователя
    var userAvatarArray: MutableList<String> = mutableListOf()

    //создаем массив, в котором будут храниться данные о аватаре пользователя при недоступности сервера
    var userAvatarArrayOffLine: MutableList<String> = mutableListOf()

    //переменная, в которой будет храниться значение открываемой страницы
    var pageNumber: Int = 1

    //переменная, в которой будет храниться значение текущей страницы на момент недоступности сревиса на сервере
    var pageNumberOnPause: Int = 0

    //переменная, в которой будет хранится данные о ввдееном логине пользователя для поиска
    var userSearch: String = ""

    // переменная для хранения ссылки к API серверу
    val baseUrl = "https://api.github.com/"

    //инициализация переменных для элементов Активити
    lateinit var buttonExit: Button
    lateinit var editSearch: TextView
    lateinit var buttonNext: Button
    lateinit var buttonPreview: Button
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        //устанавливаем другую тему
        setTheme(R.style.Github)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        buttonExit = findViewById(R.id.buttonExit)
        editSearch = findViewById(R.id.editSearch)
        buttonNext = findViewById(R.id.buttonNext)
        buttonPreview = findViewById(R.id.buttonPreview)

        outputListFromSearch()
    }

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //функция для обработки результатов запроса Retrofit
    fun userRetrofit() {
        var client: InterfaceAPI = retrofit.create(InterfaceAPI::class.java)
        val call: Call<Users<Item>> = client.getLoginUser("$userSearch in:login", pageNumber)
        call.enqueue(object : Callback<Users<Item>> {

            override fun onResponse(
                call: Call<Users<Item>>,
                response: Response<Users<Item>>
            ) {

                if (!response.isSuccessful) {
                    errorData()
                    pageNumber = pageNumberOnPause
                    return
                } else
                    clearAllArrayAndStartRetrofit()
                if (pageNumber == 1) {
                    buttonNext.setVisibility(View.VISIBLE)
                    buttonPreview.setVisibility(View.INVISIBLE)
                }

                val users = response.body()?.items.orEmpty()
                users.forEach {
                    userArray.add(it.loginUser)
                    userIdArray.add(it.id)
                    userAvatarArray.add(it.avatarUrl)
                }
                if (users.size < 30) {
                    buttonNext.setVisibility(View.INVISIBLE)
                }

                userArrayOffLine = userArray.toMutableList()
                userIdArrayOffLine = userIdArray.toMutableList()
                userAvatarArrayOffLine = userAvatarArray.toMutableList()
                recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<Users<Item>>, t: Throwable) {
                println(t)
            }
        }
        )
    }

    //функция для вывода списка после 600 млсекунд после ввода символа
    fun outputListFromSearch() {
        editSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        runOnUiThread(Runnable {
                            search()
                        })
                    }
                }, 600)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    //объявляем функцию обработки нажатия на кнопку buttonSearch - поиск для введеного логина на сервере github
    fun onClickExit(view: View) {
        finish()
        startActivity(Intent(this@ListActivity, ProfileActivity::class.java))
    }

    //функция начального поиска по запросу
    fun search() {
        userSearch = editSearch.text.toString()
        pageNumber = 1
        if (userArray.isEmpty()) {
            buttonPreview.setVisibility(View.INVISIBLE)
            buttonNext.setVisibility(View.INVISIBLE)
        }

        userRetrofit()
        recyclerViewFunction()
    }

    //объявляем функцию для обработки нажатия на клавишу Next
    fun onClickNext(view: View) {
        userSearch = editSearch.text.toString()
        if (pageNumber >= 1) buttonPreview.setVisibility(View.VISIBLE) else buttonPreview.setVisibility(
            View.INVISIBLE
        )
        pageNumberOnPause = pageNumber
        pageNumber++
        userRetrofit()
    }

    //объявляем функцию для обработки нажатия на клавишу Preview
    fun onClickPreview(view: View) {
        userSearch = editSearch.text.toString()
        if (pageNumber > 1) {
            pageNumberOnPause = pageNumber
            pageNumber--
            buttonNext.setVisibility(View.VISIBLE)
        }
        userRetrofit()
    }

    //объявляем функцию для выведения Тоаста в случае превышения лимита подключений за 1 минуту.
    fun errorData() {
        Toast.makeText(
            this,
            "Waiting data from server. Please wait few seconds.",
            Toast.LENGTH_SHORT
        ).show()
    }

    //объявляем функцию для очистки всех массивов для он-лайн работы, а так же запуск функции retrofit()
    fun clearAllArrayAndStartRetrofit() {
        userArray.clear()
        userIdArray.clear()
        userAvatarArray.clear()
    }

    //функция для вывода сожержимого полей ReciclerView с возможностью выбора элемента списка при последующим открытии новой активности
    fun recyclerViewFunction() {
        recyclerView = findViewById(R.id.listUserView)
        var adapter =
            RecyclerViewAdapter(this, userArray, object : RecyclerViewAdapter.MyListener {
                override fun MyClick(userArray: MutableList<String>, position: Int) {

                    val intent = Intent(this@ListActivity, InfoActivity::class.java)

                    //передача данных в другую активность
                    intent.putExtra("userSearch", userSearch)
                    intent.putExtra("login", this@ListActivity.userArray[position])
                    intent.putExtra("id", userIdArray[position])
                    intent.putExtra("avatar", userAvatarArray[position])

                    //запуск новой активности
                    startActivity(intent)
                }
            })
        //визуальное отображение текущих данных в recyclerView
        recyclerView.adapter = adapter;
    }
}

