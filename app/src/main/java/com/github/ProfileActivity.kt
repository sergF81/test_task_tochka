package com.github

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.squareup.picasso.Picasso


class ProfileActivity : AppCompatActivity() {
    var gso: GoogleSignInOptions? = null
    var gsc: GoogleSignInClient? = null
    lateinit var textName: TextView
    lateinit var textEmail: TextView
    lateinit var buttonSignOut: Button
    lateinit var buttonStart: Button
    lateinit var imageViewPhoto: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Github)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        textName = findViewById(R.id.textName)
        textEmail = findViewById(R.id.textEmail)
        imageViewPhoto = findViewById(R.id.imageViewPhoto)
        buttonSignOut = findViewById(R.id.buttonSignOut)
        buttonStart = findViewById(R.id.buttonStart)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestProfile()
            .requestEmail().build()
        gsc = GoogleSignIn.getClient(this, gso!!)

        //получение и отображение значений из профиля Гугл и присваивание их соответствующиъ переменным
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            var personPhoto = acct.photoUrl
            var personName = acct.displayName
            var personEmail = acct.email

            //присваивание свойствам textView значения перепенных Имя и Почта
            textName.setText("name: $personName")
            textEmail.setText("email: $personEmail")

            //загрузка и отображение фото из URI через Picasso
            Picasso.with(this)
                .load(personPhoto)
                .into(imageViewPhoto)
        }
        //обработка нажатия кнопки Выхода их пароля (Sign Out)
        buttonSignOut.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                signOut()
            }
        })
    }

    //функция выхода из учетки Гугла
    fun signOut() {
        gsc!!.signOut().addOnCompleteListener {
            //выход из текущей активности
            finish()
            //запуск первой активности (начального экрана)
            startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
        }
    }

    //функция запуска самого функционала поиска пользователей
    fun onClickStart(view: View) {
        //выход из текущей активности
        finish()
        //запуск другой активности со списком пользователей
        val intent = Intent(this@ProfileActivity, ListActivity::class.java)
        startActivity(intent)
    }

}

