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

package tokyo.randx.cutie.account.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tokyo.randx.cutie.account.data.repository.AccountRepository
import tokyo.randx.cutie.account.data.repository.AccountRepositoryImpl
import tokyo.randx.cutie.account.domain.usecase.GetCurrentUserUseCase
import tokyo.randx.cutie.account.domain.usecase.SignInUseCase
import tokyo.randx.cutie.account.domain.usecase.SignOutUseCase
import tokyo.randx.cutie.account.domain.usecase.SignUpUseCase

@Module
@InstallIn(SingletonComponent::class)
object ProvideModule {
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth =
        Firebase.auth
    @Provides
    fun provideSignInUseCase(accountRepository: AccountRepository): SignInUseCase =
        SignInUseCase(accountRepository)
    @Provides
    fun provideSignUpUseCase(accountRepository: AccountRepository): SignUpUseCase =
        SignUpUseCase(accountRepository)
    @Provides
    fun provideGetCurrentUserUseCase(accountRepository: AccountRepository): GetCurrentUserUseCase =
        GetCurrentUserUseCase(accountRepository)
    @Provides
    fun provideSignOutUseCase(accountRepository: AccountRepository): SignOutUseCase =
        SignOutUseCase(accountRepository)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class BindModule {
    @Binds
    abstract fun bindAuthRepository(impl: AccountRepositoryImpl): AccountRepository
}