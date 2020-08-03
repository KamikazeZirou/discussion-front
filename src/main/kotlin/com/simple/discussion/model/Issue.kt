package com.simple.discussion.model

import kotlinx.serialization.Serializable

// バックエンドのドメインクラスのコピペ
// TODO バックエンドとフロントエンドで共通化する(MVPの範囲の実装が大体完了したら対応したい)
@Serializable
data class Issue(
    val id: Int = -1,
    val title: String,
    val description: String,
    val labels: List<String> = listOf()
)
