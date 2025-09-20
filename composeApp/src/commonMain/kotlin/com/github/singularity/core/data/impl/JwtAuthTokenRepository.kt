package com.github.singularity.core.data.impl

//import com.appstractive.jwt.JWT
//import com.appstractive.jwt.from
//import com.appstractive.jwt.jwt
//import com.appstractive.jwt.sign
//import com.appstractive.jwt.signatures.hs256
//import com.appstractive.jwt.verify
//import com.github.singularity.core.data.AuthTokenRepository
//import com.github.singularity.core.data.PreferencesRepository
//import com.github.singularity.core.data.Token
//import com.github.singularity.core.shared.model.HostedSyncGroupNode
//import com.github.singularity.core.shared.model.Node
//import com.github.singularity.core.shared.model.PreferencesModel
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.IO
//import kotlinx.coroutines.SupervisorJob
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.stateIn
//
//class JwtAuthTokenRepository(
//    preferencesRepo: PreferencesRepository,
//) : AuthTokenRepository {
//
//    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
//
//    private val preferences = preferencesRepo.preferences
//        .stateIn(scope, SharingStarted.Companion.WhileSubscribed(5000), PreferencesModel())
//
//    override suspend fun generateAuthToken(node: Node): HostedSyncGroupNode {
//        jwt {
//            claims {
////                issuer = "${defaultGroup.hostedSyncGroupId}.${preferences.value.deviceId}"
//                subject = node.deviceId
//            }
//        }.sign {
//            hs256 { secret = preferences.value.appSecret }
//        }.toString()
//    }
//
//    override suspend fun getNode(token: Token): HostedSyncGroupNode? {
//        val token = JWT.Companion.from(token)
//        val isValid = token.verify {
//            hs256 { secret = preferences.value.appSecret }
////            issuer("${defaultGroup.hostedSyncGroupId}.${preferences.value.deviceId}")
//        }
//
//        if (!isValid) return null
//
//    }
//
//}
