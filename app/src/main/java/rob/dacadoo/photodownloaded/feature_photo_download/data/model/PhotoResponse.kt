package rob.dacadoo.photodownloaded.feature_photo_download.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoResponse(
    val total: Int,
    @SerialName("total_pages") val totalPages: Int,
    val results: List<PhotoResult>
)

@Serializable
data class PhotoResult(
    val id: String,
    val slug: String? = null,
    @SerialName("alternative_slugs") val alternativeSlugs: AlternativeSlugs? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
    @SerialName("promoted_at") val promotedAt: String? = null,
    val width: Int,
    val height: Int,
    val color: String? = null,
    @SerialName("blur_hash") val blurHash: String? = null,
    val description: String? = null,
    @SerialName("alt_description") val altDescription: String? = null,
    val breadcrumbs: List<String>? = null,
    val urls: Urls,
    val links: Links,
    val likes: Int? = null,
    @SerialName("liked_by_user") val likedByUser: Boolean? = null,
    @SerialName("current_user_collections") val currentUserCollections: List<String>? = null,
    val sponsorship: String? = null,
    @SerialName("topic_submissions") val topicSubmissions: Map<String, TopicSubmission>? = emptyMap(),
    @SerialName("asset_type") val assetType: String? = null,
    val user: User
)

@Serializable
data class AlternativeSlugs(
    val en: String? = null,
    val es: String? = null,
    val ja: String? = null,
    val fr: String? = null,
    val it: String? = null,
    val ko: String? = null,
    val de: String? = null,
    val pt: String? = null
)

@Serializable
data class Urls(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String,
    @SerialName("small_s3") val smallS3: String? = null
)

@Serializable
data class Links(
    val self: String,
    val html: String,
    val download: String,
    @SerialName("download_location") val downloadLocation: String
)

@Serializable
data class User(
    val id: String,
    @SerialName("updated_at") val updatedAt: String? = null,
    val username: String,
    val name: String,
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null,
    @SerialName("twitter_username") val twitterUsername: String? = null,
    @SerialName("portfolio_url") val portfolioUrl: String? = null,
    val bio: String? = null,
    val location: String? = null,
    val links: UserLinks,
    @SerialName("profile_image") val profileImage: ProfileImage,
    @SerialName("instagram_username") val instagramUsername: String? = null,
    @SerialName("total_collections") val totalCollections: Int? = null,
    @SerialName("total_likes") val totalLikes: Int? = null,
    @SerialName("total_photos") val totalPhotos: Int? = null,
    @SerialName("total_promoted_photos") val totalPromotedPhotos: Int? = null,
    @SerialName("total_illustrations") val totalIllustrations: Int? = null,
    @SerialName("total_promoted_illustrations") val totalPromotedIllustrations: Int? = null,
    @SerialName("accepted_tos") val acceptedTos: Boolean? = null,
    @SerialName("for_hire") val forHire: Boolean? = null,
    val social: Social? = null
)

@Serializable
data class UserLinks(
    val self: String,
    val html: String,
    val photos: String,
    val likes: String,
    val portfolio: String,
    val following: String? = null,
    val followers: String? = null
)

@Serializable
data class ProfileImage(
    val small: String,
    val medium: String,
    val large: String
)

@Serializable
data class TopicSubmission(
    val status: String,
    @SerialName("approved_on") val approvedOn: String
)

@Serializable
data class Social(
    @SerialName("instagram_username") val instagramUsername: String? = null,
    @SerialName("portfolio_url") val portfolioUrl: String? = null,
    @SerialName("twitter_username") val twitterUsername: String? = null,
    @SerialName("paypal_email") val paypalEmail: String? = null
)