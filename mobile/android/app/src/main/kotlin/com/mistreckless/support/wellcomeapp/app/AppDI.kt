package com.mistreckless.support.wellcomeapp.app

import android.app.Application
import android.content.Context
import com.mistreckless.support.wellcomeapp.navigation.NavigationModule
import com.wellcome.share.CameraActivity
import com.wellcome.share.CameraModule
import com.wellcomeapp.main.MainActivity
import com.wellcomeapp.main.MainActivityModule
import com.wellcomeapp.main.data.DataModule
import com.wellcomeapp.main.data.RepositoryModule
import com.wellcomeapp.main.domain.InteractorModule
import com.wellcomeapp.ui_core.PerActivity
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import java.util.*
import javax.inject.Singleton

/**
 * Created by @mistreckless on 05.08.2017. !
 */

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
    @ContributesAndroidInjector(
        modules = [(MainActivityFragmentProvider::class), (MainActivityModule::class)]
    )
    abstract fun bindMainActivity(): MainActivity

    @PerActivity
    @ContributesAndroidInjector(
        modules = [(CameraActivityFragmentProvider::class), (CameraModule::class)]
    )
    abstract fun bindCameraActivity(): CameraActivity
}

@Singleton
@Component(modules = [(AndroidInjectionModule::class), (AppModule::class), (DataModule::class), (RepositoryModule::class), (InteractorModule::class), (NavigationModule::class), (ActivityBuilder::class)])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance fun application(application: Application): Builder
        fun dataModule(dataModule: DataModule): Builder
        fun repositoryModule(repositoryModule: RepositoryModule): Builder
        fun interactorModule(interactorModule: InteractorModule): Builder
        fun navigationModule(navigationModule: NavigationModule): Builder
        fun build(): AppComponent
    }

    fun inject(app: App)
}