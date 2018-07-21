package com.wellcome.wellcomeapp.app

import android.app.Application
import android.content.Context
import com.wellcome.main.MainActivity
import com.wellcome.main.MainActivityModule
import com.wellcome.share.CameraActivity
import com.wellcome.share.CameraModule
import com.wellcome.ui.core.PerActivity
import com.wellcome.wellcomeapp.navigation.NavigationModule
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import wellcome.common.core.CommonModule
import java.util.*
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun providesContext(application: Application): Context = application

    @Singleton
    @Provides
    fun provideLocale(): Locale = Locale.ENGLISH
}

@Module
abstract class ActivityBuilder {

    @PerActivity
    @ContributesAndroidInjector(modules = [(MainActivityFragmentProvider::class), (MainActivityModule::class)])
    abstract fun bindMainActivity(): MainActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [(CameraActivityFragmentProvider::class), (CameraModule::class)])
    abstract fun bindCameraActivity(): CameraActivity
}

@Singleton
@Component(modules = [(AndroidInjectionModule::class), (AppModule::class), (CommonModule::class), (RepositoryModule::class), (NavigationModule::class), (ActivityBuilder::class)])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun commonModule(commonModule: CommonModule): Builder
        fun repositoryModule(repositoryModule: RepositoryModule): Builder
        fun navigationModule(navigationModule: NavigationModule): Builder
        fun build(): AppComponent
    }

    fun inject(app: App)
}