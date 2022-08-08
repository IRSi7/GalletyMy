package space.irsi7.gallerymy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import space.irsi7.gallerymy.databinding.ActivityMainBinding
import space.irsi7.gallerymy.databinding.ActivityRenameBinding

class RenameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRenameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRenameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.oldname.text = intent.getStringExtra("old")

        binding.btnRename.setOnClickListener() {
            val intent = Intent(this, RenameActivity::class.java).apply {
                putExtra(Constants.FILE_NAME, binding.edit.text.toString())
                startActivity(intent)
                finish()
            }
        }
        binding.btnCancel.setOnClickListener() {
            val intent = Intent(this, RenameActivity::class.java).apply {
                startActivity(intent)
                finish()
            }
        }
    }
}