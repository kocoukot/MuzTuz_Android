package com.artline.muztus.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import com.artline.muztus.databinding.ActivityMainBinding
import com.artline.muztus.sounds.GameSound
import com.artline.muztus.sounds.MusicPlayerService
import com.artline.muztus.sounds.SoundsPlayerService
import com.muztus.core.ext.SupportInfoBar
import com.muztus.core.ext.castSafe
import com.muztus.core_mvi.UpdateCoins
import com.muztus.domain_layer.model.IGameSound
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), UpdateCoins, com.artline.muztus.sounds.GameSoundPlay {

    private val soundsPlayerService = SoundsPlayerService()
    private val musicPlayerService: MusicPlayerService = MusicPlayerService(this)
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    private val navHost by lazy {
        supportFragmentManager.findFragmentById(binding.navHostView.id)
            .castSafe<NavHostFragment>()
    }

    private val currentVisibleFragment: Fragment?
        get() = navHost?.childFragmentManager?.fragments?.first()

    private val onBackStackChangedListener by lazy {
        FragmentManager.OnBackStackChangedListener {
            binding.infoLayout.isVisible = currentVisibleFragment is SupportInfoBar
        }
    }

    override fun onResume() {
        super.onResume()
        FragmentManager.OnBackStackChangedListener { }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            ActivityMainBinding.inflate(layoutInflater)
                .also { binding = it }
                .root
        )

        navHost?.apply {
            childFragmentManager.addOnBackStackChangedListener(onBackStackChangedListener)
        }


        with(binding) {
            imageView5.setOnClickListener {
                viewModel.addCoins()
            }

            menuMusic.apply {
                isActivated = true
            }
            menuSound.apply {
                isActivated = true
            }

            menuMusic.apply {
                setOnClickListener {
                    if (isActivated) musicPlayerService.pause() else musicPlayerService.resume(this@MainActivity)
                    viewModel.soundChange(IGameSound.GameMusic)
                }
            }

            menuSound.apply {
                setOnClickListener {
                    playGameSound(if (isActivated) GameSound.SoundOnMusic else GameSound.SoundOffMusic)
                    viewModel.soundChange(IGameSound.GameSound)
                }
            }
        }
        //todo fix remove after test
        observeLiveData()
    }


    private fun observeLiveData() {
        viewModel.coins.observe(this) { mainInfo ->
            binding.menuCoins.text = mainInfo.coinsAmount.toString()
            binding.menuStars.text = mainInfo.starsAmount.toString()
        }

        viewModel.sounds.observe(this) { soundsInfo ->
            println("sound activity $soundsInfo")

            binding.menuMusic.apply {
                isActivated = soundsInfo.musicState.soundState()
                if (isActivated) {
                    musicPlayerService.start(this@MainActivity)
                }
            }

            binding.menuSound.apply {
                isActivated = soundsInfo.soundState.soundState()
            }
        }
    }


    fun infoBarVisibility(isVisible: Boolean) {
        binding.infoLayout.isVisible = isVisible
    }

    override fun updateCoins() {
        viewModel.updateCoins()
    }

    override fun playGameSound(soundType: GameSound) {
        soundsPlayerService.start(this@MainActivity, soundType)
    }
}


