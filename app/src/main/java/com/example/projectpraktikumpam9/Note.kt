package com.example.projectpraktikumpam9

class Note {
    var title: String? = null
    var description: String? = null
    var id: String? = null // Tambahkan ini

    constructor() {}

    constructor(title: String?, description: String?) {
        this.title = title
        this.description = description
    }
}
