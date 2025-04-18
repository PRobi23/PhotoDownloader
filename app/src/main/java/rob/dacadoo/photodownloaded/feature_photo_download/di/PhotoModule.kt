package rob.dacadoo.photodownloaded.feature_photo_download.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import rob.dacadoo.photodownloaded.feature_photo_download.data.DefaultPhotoRepository
import rob.dacadoo.photodownloaded.feature_photo_download.domain.PhotoRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class PhotoModule {
    @Binds
    abstract fun bindPhotoRepository(
        impl: DefaultPhotoRepository
    ): PhotoRepository
}
