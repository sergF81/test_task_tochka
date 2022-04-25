package com.github


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import android.widget.Toast
import com.google.android.gms.common.api.ApiException


class MainActivity : AppCompatActivity() {
    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient
    lateinit var buttonSignIn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Github)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonSignIn = findViewById(R.id.buttonSignIn)

        //Конфигурация Sign-in запроса в профиле для значений user's ID, email address, photo
        gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestProfile()
                .requestEmail().build()
        gsc = GoogleSignIn.getClient(this, gso)
        var acct: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {

        }
        //обработка нажатия кнопки для авторизации в Гугл профиле
        buttonSignIn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                signIn()
            }
        })
    }

    fun signIn() {
        var signInIntent: Intent = gsc.getSignInIntent()
        startActivityForResult(signInIntent, 1000)

    }

    //перезапись функции запуска активити, которая возвращает результат
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === 1000) {
            var task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                task.getResult(ApiException::class.java)
                //закрытие текущей активности
                finish()
                //открытие активности с Профилей пользователя Гулга
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivity(intent)
            } catch (e: ApiException) {
                println(e)
                //Если что-то пошло не так, то выходит Toast
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
