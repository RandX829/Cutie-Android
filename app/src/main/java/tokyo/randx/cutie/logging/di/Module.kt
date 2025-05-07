/*
 * Copyright 2025 RandX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tokyo.randx.cutie.logging.di

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tokyo.randx.cutie.logging.data.LogRepositoryImpl
import tokyo.randx.cutie.logging.data.LogRepository
import tokyo.randx.cutie.logging.domain.LogErrorUseCase

@Module
@InstallIn(SingletonComponent::class)
object ProvideModule {
    @Provides
    fun provideCrashlytics(): FirebaseCrashlytics = Firebase.crashlytics
    @Provides
    fun provideLogErrorUseCase(logRepository: LogRepository): LogErrorUseCase =
        LogErrorUseCase(logRepository)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class BindModule {
    @Binds
    abstract fun bindLogRepository(impl: LogRepositoryImpl): LogRepository
}