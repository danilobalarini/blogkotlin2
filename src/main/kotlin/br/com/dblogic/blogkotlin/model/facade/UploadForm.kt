package br.com.dblogic.blogkotlin.model.facade

import org.springframework.web.multipart.MultipartFile

class UploadForm(val id: Long, 
                 var coverImage: MutableList<MultipartFile> = mutableListOf<MultipartFile>()) {
}