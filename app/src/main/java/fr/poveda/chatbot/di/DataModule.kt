package fr.poveda.chatbot.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.poveda.chatbot.data.DefaultRepository
import fr.poveda.chatbot.data.IRepository
import fr.poveda.chatbot.data.source.network.INetworkDataSource
import fr.poveda.chatbot.data.source.network.MockedNetworkServer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindTaskRepository(repository: DefaultRepository): IRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceNetworkModule {

    @Singleton
    @Binds
    abstract fun bindNetworkDataSource(dataSource: MockedNetworkServer): INetworkDataSource
}
