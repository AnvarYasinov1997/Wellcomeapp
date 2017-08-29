package com.mistreckless.support.wellcomeapp.app

import android.app.Application
import android.content.Context
import com.mistreckless.support.wellcomeapp.domain.DataModule
import com.mistreckless.support.wellcomeapp.domain.InteractorModule
import com.mistreckless.support.wellcomeapp.domain.RepositoryModule
import com.mistreckless.support.wellcomeapp.ui.MainActivity
import com.mistreckless.support.wellcomeapp.ui.MainActivityFragmentProvider
import com.mistreckless.support.wellcomeapp.ui.MainActivityModule
import com.mistreckless.support.wellcomeapp.ui.PerActivity
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

/**
 * Created by @mistreckless on 05.08.2017. !
 */

@Singleton
@Module
class AppModule {

    @Provides
    fun providesContext(application: Application) : Context = application
}

@PerActivity
@Module
abstract class ActivityBuilder {

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(MainActivityFragmentProvider::class, MainActivityModule::class))
    abstract fun bindActivity(): MainActivity
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