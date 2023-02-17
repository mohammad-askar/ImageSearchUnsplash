package com.example.imagesearchunsplash

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.imagesearchunsplash.data.model.UnsplashPhoto
import com.example.imagesearchunsplash.databinding.FragmentDetailBinding
import com.example.imagesearchunsplash.presentation.adapter.UnsplashPhotoAdapter


class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val args by navArgs<DetailFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDetailBinding.bind(view)

        // start of Glide Listener
        var glideListener = object : RequestListener<Drawable> {

            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                binding.progressBar.isVisible = false
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                val photo = args.photo
                binding.progressBar.isVisible = false
                binding.creatorTextView.isVisible = true
                binding.descriptionTextView.isVisible = photo.description != null
                return false
            }
        } // end of glide listener

        binding.apply {
            val photo = args.photo
            Glide.with(this@DetailFragment)
                .load(photo.urls.full)
                .listener(glideListener)
                .error(R.drawable.error_24)
                .into(imageView)

            descriptionTextView.text = photo.description
            val uri = Uri.parse(photo.user.attributionUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri)

            creatorTextView.apply {
                text = photo.user.name
                setOnClickListener {
                    context.startActivity(intent)
                }
                paint.isUnderlineText = true
            }
        }
    }
}
