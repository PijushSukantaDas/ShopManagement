package com.example.shopmanagement.di

import com.example.shopmanagement.data.dao.CategoryDao
import com.example.shopmanagement.data.dao.ICategoryDao
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DaoModule {
    @Binds
    abstract fun getCategoryDao(categoryDao: CategoryDao) : ICategoryDao
}