package app.rtcgarmentex.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.rtcgarmentex.databinding.ActivityAddReceivingBinding

class AddReceivingActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityAddReceivingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddReceivingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }
}