package com.github


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


class InfoActivity : AppCompatActivity() {

    //переменная для хранения значения login пользователя, данные о котором выводим
    var login: String = ""

    //переменная для хранения значения id пользователя, данные о котором выводим
    var id: String = ""

    //переменная для хранения значения avatar пользователя, данные о котором выводим
    var avatar: String = ""

    //переменная для хранения значения запроса поиска
    var userSearch: String = ""

    lateinit var textViewGeneral: TextView
    lateinit var textViewLogin: TextView
    lateinit var textViewId: TextView
    lateinit var textViewAvatar: TextView
    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Github)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        textViewGeneral = findViewById(R.id.textViewGeneral)
        textViewLogin = findViewById(R.id.textViewLogin)
        textViewId = findViewById(R.id.textViewId)
        textViewAvatar = findViewById(R.id.textViewAvatar)
        imageView = findViewById(R.id.imageView)

        //получаем данные из главной активности и присваиваем их соответствующим переменным
        login = intent.getStringExtra("login").toString()
        id = intent.getStringExtra("id").toString()
        avatar = intent.getStringExtra("avatar").toString()
        userSearch = intent.getStringExtra("userSearch").toString()

        //присваеваем новые значения текстовым View
        textViewLogin.setText("Login user: $login")
        textViewId.setText("ID user: $id")

        //выводим изображение из ссылки в переменной avatar
        Picasso.with(this)
            .load(avatar)
            .into(imageView);
    }

    //функция закртытия текущей активности с личными данными выбранного пользователя
    fun onClickReturn(view: View) {
        this.finish()
    }
}




