package com.mistreckless.support.wellcomeapp.app

import android.app.Application
import android.content.Context
import com.mistreckless.support.wellcomeapp.data.DataModule
import com.mistreckless.support.wellcomeapp.data.RepositoryModule
import com.mistreckless.support.wellcomeapp.domain.InteractorModule
import com.mistreckless.support.wellcomeapp.ui.MainActivity
import com.mistreckless.support.wellcomeapp.ui.MainActivityFragmentProvider
import com.mistreckless.support.wellcomeapp.ui.MainActivityModule
import com.mistreckless.support.wellcomeapp.ui.PerActivity
import com.mistreckless.support.wellcomeapp.ui.screen.camera.CameraActivity
import com.mistreckless.support.wellcomeapp.ui.screen.camera.CameraActivityFragmentProvider
import com.mistreckless.support.wellcomeapp.ui.screen.camera.CameraModule
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

@Singleton
@Module
class AppModule {

    @Singleton
    @Provides
    fun providesContext(application: Application) : Context = application

    @Singleton
    @Provides
    fun provideLocale() : Locale = Locale.ENGLISH

}

@PerActivity
@Module
abstract class ActivityBuilder {

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(MainActivityFragmentProvider::class, MainActivityModule::class))
    abstract fun bindMainActivity(): MainActivity

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(CameraActivityFragmentProvider::class,CameraModule::class))
    abstract fun bindCameraActivity() : CameraActivity
}

@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        AppModule::class,
        DataModule::class,
        RepositoryModule::class,
        InteractorModule::class,
        ActivityBuilder::class))
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance fun application(application: Application) : Builder
        fun dataModule(dataModule: DataModule) : Builder
        fun repositoryModule(repositoryModule: RepositoryModule) : Builder
        fun interactorModule(interactorModule: InteractorModule) : Builder
        fun build(): AppComponent
    }

    fun inject(app: App)
}