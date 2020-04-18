package ru.kolesnikovdmitry.lesson13sounds

import android.annotation.SuppressLint
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var mSoundPool    : SoundPool
    private lateinit var mAssetManager : AssetManager
    private var mCowSound     = 0
    private var mChickenSound = 0
    private var mDogSound     = 0
    private var mCatSound     = 0
    private var mDuckSound    = 0
    private var mSheepSound   = 0
    private lateinit var mBtnDuck    : Button
    private lateinit var mBtnCat     : Button
    private lateinit var mBtnDog     : Button
    private lateinit var mBtnSheep   : Button
    private lateinit var mBtnChicken : Button
    private lateinit var mBtnCow     : Button
    private var mStreamID = 0

    private var mLastBtn = 0 //1- cow, 2 - sheep, 3 - dog, 4 - cat, 5 - duck, 6 - chicken
    private var mNumberPress = 0

    @SuppressLint("ClickableViewAccessibility")
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            createOldSoundPool() // если бы у нас была поддержка старых устройств.
        }
        else {
            createNewSoundPool()
        }

        mAssetManager = assets

        //Получаем идентификаоры
        mCatSound     = loadSound("cat.mp3")
        mDogSound     = loadSound("dog.mp3")
        mCowSound     = loadSound("cow.mp3")
        mChickenSound = loadSound("chicken.mp3")
        mSheepSound   = loadSound("sheep.mp3")
        mDuckSound    = loadSound("duck.mp3")

        mBtnCat     = findViewById(R.id.btnCat)
        mBtnChicken = findViewById(R.id.btnChicken)
        mBtnCow     = findViewById(R.id.btnCow)
        mBtnDog     = findViewById(R.id.btnDog)
        mBtnDuck    = findViewById(R.id.btnDuck)
        mBtnSheep   = findViewById(R.id.btnSheep)

        mBtnSheep.setOnTouchListener { btn, event ->
            val eventAction = event.action
            when(eventAction) {
                MotionEvent.ACTION_UP ->
                    if(mStreamID > 0) {
                        mSoundPool.stop(mStreamID)
                    }
                MotionEvent.ACTION_DOWN ->
                    mStreamID = playSound(mSheepSound)
                MotionEvent.ACTION_CANCEL ->
                    mSoundPool.stop(mStreamID)
            }
            return@setOnTouchListener true
        }
        mBtnDuck.setOnClickListener {btn ->
            if(mLastBtn == 5) {
                if(mNumberPress == 1) {
                    mNumberPress++
                    playSound(mDuckSound)
                }
                else {
                    mNumberPress = 1
                    mSoundPool.stop(mStreamID)
                }
            }
            else {
                mNumberPress = 2;
                playSound(mDuckSound)
            }
            mLastBtn = 5
        }
        mBtnDog.setOnClickListener {
            if(mLastBtn == 3) {
                if(mNumberPress == 1) {
                    mNumberPress++
                    playSound(mDogSound)
                }
                else {
                    mNumberPress = 1
                    mSoundPool.stop(mStreamID)
                }
            }
            else {
                mNumberPress = 2;
                playSound(mDogSound)
            }
            mLastBtn = 3
        }
        mBtnCow.setOnClickListener {
            if(mLastBtn == 1) {
                if(mNumberPress == 1) {
                    mNumberPress++
                    playSound(mCowSound)
                }
                else {
                    mNumberPress = 1
                    mSoundPool.stop(mStreamID)
                }
            }
            else {
                mNumberPress = 2
                playSound(mCowSound)
            }
            mLastBtn = 1
        }
        mBtnChicken.setOnClickListener {
            if(mLastBtn == 6) {
                if(mNumberPress == 1) {
                    mNumberPress++
                    playSound(mChickenSound)
                }
                else {
                    mNumberPress = 1
                    mSoundPool.stop(mStreamID)
                }
            }
            else {
                mNumberPress = 2
                playSound(mChickenSound)
            }
            mLastBtn = 6
        }
        mBtnCat.setOnClickListener {
            if(mLastBtn == 4) {
                if(mNumberPress == 1) {
                    mNumberPress++
                    playSound(mCatSound)
                }
                else {
                    mNumberPress = 1
                    mSoundPool.stop(mStreamID)
                }
            }
            else {
                mNumberPress = 2
                playSound(mCatSound)
            }
            mLastBtn = 4
        }






    }

    override fun onResume() {
        super.onResume()
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            createOldSoundPool()
        }
        else {
            createNewSoundPool()
        }

        mAssetManager = assets

        mCatSound = loadSound("cat.mp3");
        mChickenSound = loadSound("chicken.mp3");
        mCowSound = loadSound("cow.mp3");
        mDogSound = loadSound("dog.mp3");
        mDuckSound = loadSound("duck.mp3");
        mSheepSound = loadSound("sheep.mp3");
    }

    override fun onPause() {
        super.onPause()
        mSoundPool.release()
    }

    private fun playSound(sound: Int): Int {
        if(sound > 0) {
            mStreamID = mSoundPool.play(sound, 1F, 1F, 1, 0, 1F)
        }
        return mStreamID
    }

    private fun createNewSoundPool() {
        val attr = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        mSoundPool = SoundPool.Builder()
            .setAudioAttributes(attr)
            .build()

    }

    private fun createOldSoundPool() {
        mSoundPool = SoundPool(3, AudioManager.STREAM_MUSIC, 0)
    }

    private fun loadSound(fileName: String): Int {
        val afd: AssetFileDescriptor
        try {
            afd = mAssetManager.openFd(fileName)
        } catch (th: Throwable) {
            Snackbar.make(mBtnCat, th.message.toString(), Snackbar.LENGTH_LONG).show()
            return -1;
        }
        return mSoundPool.load(afd, 1);
    }
}