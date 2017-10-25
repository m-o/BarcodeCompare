package cz.twisto.comparator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_main.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import android.media.AudioManager
import android.media.ToneGenerator
import android.view.View


class MainActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    var firstCodeVal: String? = null
    var secondCodeVal: String? = null

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        setContentView(R.layout.activity_main)

        continueButton.visibility = View.GONE
        resetButton.visibility = View.GONE

        continueButton.setOnClickListener {
            mScannerView!!.resumeCameraPreview(this)
        }

        resetButton.setOnClickListener {
            firstCode.text = "Prvni :"
            secondCode.text = "Druhy :"
            shodaValue.text = "Cekam"
            firstCodeVal = null
            secondCodeVal = null
            mScannerView!!.resumeCameraPreview(this)
            continueButton.visibility = View.GONE
            resetButton.visibility = View.GONE
        }
    }

    public override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera()
    }

    public override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()
    }

    override fun handleResult(rawResult: Result) {
        val loadingNumber = rawResult.text
        if(loadingNumber.length == 14){
            if(firstCodeVal == null){
                firstCodeVal = loadingNumber
                firstCode.text = """Prvni :$firstCodeVal"""
                continueButton.visibility = View.VISIBLE
            }
            else if(secondCodeVal == null){
                secondCodeVal = loadingNumber
                secondCode.text = """Druhy :$secondCodeVal"""
                if(secondCodeVal == firstCodeVal){
                    shodaValue.text = "Shoda"
                    playPositiveSound()
                }
                else{
                    shodaValue.text = "CHYBA"
                    playNegativeSound()
                }
                continueButton.visibility = View.GONE
                resetButton.visibility = View.VISIBLE
            }
        }
        else{
            mScannerView!!.resumeCameraPreview(this)
        }

    }

    fun playPositiveSound(){
        val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME)
        ToneGenerator.MAX_VOLUME
        if (toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP2)) {
            Thread.sleep(400)
            toneGenerator.stopTone();
        }
    }

    fun playNegativeSound(){
        val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME)
        ToneGenerator.MAX_VOLUME
        if (toneGenerator.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT)) {
            Thread.sleep(400)
            toneGenerator.stopTone();
        }
    }


}
