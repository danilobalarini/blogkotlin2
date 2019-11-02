package br.com.dblogic.blogkotlin.model

import org.springframework.web.multipart.MultipartFile

class UploadForm(val id: Long, 
                 var coverImage: MutableList<MultipartFile> = mutableListOf<MultipartFile>()) {
}