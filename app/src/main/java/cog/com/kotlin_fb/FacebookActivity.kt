package cog.com.kotlin_fb

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import java.util.*

class FacebookActivity : AppCompatActivity() {

    lateinit var facebook_login: Button
    lateinit var facebook_logout: Button
    lateinit var id: TextView
    lateinit var name: TextView
    lateinit var link: TextView
    lateinit var email: TextView
    lateinit var picture: ImageView
    lateinit var gender: TextView
    lateinit var birthday: TextView
    private var callbackManager: CallbackManager? = null
    var a = ""
    var b = ""
    var c = ""
    var d = ""
    var e = ""
    var f = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook)

        /* val info: PackageInfo
         try {
             info = packageManager.getPackageInfo("com.example.kotlin.myapplication", PackageManager.GET_SIGNATURES)
             for (signature in info.signatures) {
                 val md = MessageDigest.getInstance("SHA")
                 md.update(signature.toByteArray())
                 Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
             }
         } catch (e: PackageManager.NameNotFoundException) {
             e.printStackTrace()
         } catch (e: Exception) {
             e.printStackTrace()
         }*/

        FacebookSdk.sdkInitialize(applicationContext)
        callbackManager = CallbackManager.Factory.create()
        faceBookInitialize()

        facebook_login = findViewById<Button>(R.id.facebook_login)
        facebook_logout = findViewById<Button>(R.id.facebook_logout)
        id = findViewById<TextView>(R.id.id)
        name = findViewById<TextView>(R.id.name)
        email = findViewById<TextView>(R.id.email)
        picture = findViewById<ImageView>(R.id.picture)
        gender = findViewById<TextView>(R.id.gender)



        facebook_login.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this@FacebookActivity, Arrays.asList("email", "user_photos", "public_profile")) }

        facebook_logout.setOnClickListener(View.OnClickListener {
            // Logout
            if (AccessToken.getCurrentAccessToken() != null) {
                GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, GraphRequest.Callback {
                    AccessToken.setCurrentAccessToken(null)
                    LoginManager.getInstance().logOut()

                    id.setText("")
                    name.setText("")
                    email.setText("")
                    gender.setText("")
                    facebook_logout.visibility = View.INVISIBLE
                    facebook_login.visibility = View.VISIBLE

                    finish()


                }).executeAsync()
            }
        })
    }

    fun faceBookInitialize() {

        println("yess on fcbk")
        LoginManager.getInstance().registerCallback(callbackManager!!, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                println("=========================onsuccess")
                val accessToken = AccessToken.getCurrentAccessToken()
                val request = GraphRequest.newMeRequest(accessToken) { `object`, response ->
                    println("===================JSON++" + `object`)
                    println("===================JSON++" + `object`.toString())

                    var SfacebookID = ""
                    var Sname = ""
                    var Semail = ""
                    var Sgender = ""
                    var Surl = ""
                    val Sphone = ""

                    try {

                        if (`object`.has("id")) {
                            SfacebookID = `object`.getString("id")
                            a = SfacebookID
                            println("id is $SfacebookID & $a")
                        }

                        if (`object`.has("name")) {
                            Sname = `object`.getString("name")
                            b = Sname
                            println("id is $Sname & $b")
                        }

                        if (`object`.has("email")) {
                            Semail = `object`.getString("email")
                            c = Semail
                            println("id is $Semail & $c")
                        }

                        if (`object`.has("gender")) {
                            Sgender = `object`.getString("gender")
                            d = Sgender
                            println("id is $Sgender & $d")
                        }

                        if (`object`.has("picture")) {
                            Surl = `object`.getJSONObject("picture").getJSONObject("data").getString("url")
                            e = Surl
                            println("id is $Surl & $e")
                        }

                        id.visibility = View.VISIBLE
                        name.visibility = View.VISIBLE
                        gender.visibility = View.VISIBLE
                        email.visibility = View.VISIBLE
                        picture.visibility = View.VISIBLE
                        //gender.visibility = View.VISIBLE
                        //birthday.visibility = View.VISIBLE
                        id.setText("Id - $a")
                        name.setText("Name - $b")
                        email.setText("Email - $c")
                        gender.setText("Gender - $d")
                        //picture.setImageURI()
                        Glide.with(this@FacebookActivity).load(e).into(picture)
                        //gender.setText("Id - $e")
                        //birthday.setText("Id - $f")

                        facebook_login.visibility = View.INVISIBLE
                        facebook_logout.visibility = View.VISIBLE

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                val parameters = Bundle()
                parameters.putString("fields", "id,name,link,email,picture,gender, birthday")
                request.parameters = parameters
                request.executeAsync()

            }

            override fun onCancel() {
                //TODO Auto-generated method stub
                println("=========================onCancel")
                Toast.makeText(this@FacebookActivity, "Cancel", Toast.LENGTH_LONG).show()
            }

            override fun onError(error: FacebookException) {
                //TODO Auto-generated method stub
                println("=========================onError" + error.toString())
                Toast.makeText(this@FacebookActivity, "onError", Toast.LENGTH_LONG).show()
            }
        })
    }


    override fun onActivityResult(requestCode: Int, responseCode: Int, intent: Intent) {

        callbackManager!!.onActivityResult(requestCode, responseCode, intent)
    }
}