package com.example.dailyscribe.data.local

import com.example.dailyscribe.data.local.entity.BibleVerseEntity

object BibleSeeder {

    fun getInitialVerses(): List<BibleVerseEntity> {
        val verses = mutableListOf<BibleVerseEntity>()

        // Helper to add verse
        fun add(translation: String, book: String, chapter: Int, verse: Int, text: String) {
            verses.add(
                BibleVerseEntity(
                    id = "${translation}_${book}_${chapter}_${verse}",
                    translationId = translation,
                    book = book,
                    chapter = chapter,
                    verse = verse,
                    text = text
                )
            )
        }

        // --- BEREAN STANDARD BIBLE (BSB) ---
        // Genesis 1
        add("BSB", "Genesis", 1, 1, "In the beginning God created the heavens and the earth.")
        add("BSB", "Genesis", 1, 2, "Now the earth was formless and void, darkness was over the surface of the deep, and the Spirit of God was hovering over the waters.")
        add("BSB", "Genesis", 1, 3, "And God said, 'Let there be light,' and there was light.")
        add("BSB", "Genesis", 1, 4, "And God saw that the light was good, and He separated the light from the darkness.")
        add("BSB", "Genesis", 1, 5, "God called the light 'day,' and the darkness He called 'night.' And there was evening, and there was morning—the first day.")

        // Psalms 23
        add("BSB", "Psalms", 23, 1, "The LORD is my shepherd; I shall not want.")
        add("BSB", "Psalms", 23, 2, "He makes me lie down in green pastures. He leads me beside quiet waters.")
        add("BSB", "Psalms", 23, 3, "He restores my soul. He guides me in the paths of righteousness for His name's sake.")
        add("BSB", "Psalms", 23, 4, "Even though I walk through the valley of the shadow of death, I will fear no evil, for You are with me; Your rod and Your staff, they comfort me.")
        add("BSB", "Psalms", 23, 5, "You prepare a table before me in the presence of my enemies. You anoint my head with oil; my cup overflows.")
        add("BSB", "Psalms", 23, 6, "Surely goodness and mercy will follow me all the days of my life, and I will dwell in the house of the LORD forever.")

        // Matthew 1
        add("BSB", "Matthew", 1, 1, "The record of the genealogy of Jesus Christ, the son of David, the son of Abraham:")
        add("BSB", "Matthew", 1, 2, "Abraham was the father of Isaac, Isaac the father of Jacob, Jacob the father of Judah and his brothers:")
        add("BSB", "Matthew", 1, 3, "Judah was the father of Perez and Zerah (whose mother was Tamar), Perez the father of Hezron, and Hezron the father of Ram.")

        // John 1
        add("BSB", "John", 1, 1, "In the beginning was the Word, and the Word was with God, and the Word was God.")
        add("BSB", "John", 1, 2, "He was with God in the beginning.")
        add("BSB", "John", 1, 3, "Through Him all things were made, and without Him nothing was made that has been made.")
        add("BSB", "John", 1, 4, "In Him was life, and that life was the light of men.")
        add("BSB", "John", 1, 5, "The light shines in the darkness, and the darkness has not overcome it.")

        // Romans 8
        add("BSB", "Romans", 8, 1, "Therefore, there is now no condemnation for those who are in Christ Jesus.")
        add("BSB", "Romans", 8, 2, "For in Christ Jesus the law of the Spirit of life set you free from the law of sin and death.")
        add("BSB", "Romans", 8, 3, "For what the law was powerless to do in that it was weakened by the flesh, God did by sending His own Son in the likeness of sinful flesh as an offering for sin.")
        add("BSB", "Romans", 8, 4, "He condemned sin in the flesh, so that the righteous requirement of the law might be fully met in us, who do not walk according to the flesh, but according to the Spirit.")


        // --- WORLD ENGLISH BIBLE (WEB) ---
        // Genesis 1
        add("WEB", "Genesis", 1, 1, "In the beginning, God created the heavens and the earth.")
        add("WEB", "Genesis", 1, 2, "The earth was formless and empty. Darkness was on the surface of the deep. God's Spirit was hovering over the surface of the waters.")
        add("WEB", "Genesis", 1, 3, "God said, 'Let there be light,' and there was light.")
        add("WEB", "Genesis", 1, 4, "God saw the light, and saw that it was good. God separated the light from the darkness.")
        add("WEB", "Genesis", 1, 5, "God called the light Day, and the darkness he called Night. There was evening and there was morning, the first day.")

        // Psalms 23
        add("WEB", "Psalms", 23, 1, "Yahweh is my shepherd: I shall lack nothing.")
        add("WEB", "Psalms", 23, 2, "He makes me lie down in green pastures. He leads me beside still waters.")
        add("WEB", "Psalms", 23, 3, "He restores my soul. He guides me in the paths of righteousness for his name's sake.")
        add("WEB", "Psalms", 23, 4, "Even though I walk through the valley of the shadow of death, I will fear no evil, for you are with me. Your rod and your staff, they comfort me.")
        add("WEB", "Psalms", 23, 5, "You prepare a table before me in the presence of my enemies. You anoint my head with oil. My cup runs over.")
        add("WEB", "Psalms", 23, 6, "Surely goodness and loving kindness shall follow me all the days of my life, and I shall dwell in Yahweh's house forever.")

        // Matthew 1
        add("WEB", "Matthew", 1, 1, "The book of the genealogy of Jesus Christ, the son of David, the son of Abraham.")
        add("WEB", "Matthew", 1, 2, "Abraham became the father of Isaac. Isaac became the father of Jacob. Jacob became the father of Judah and his brothers.")
        add("WEB", "Matthew", 1, 3, "Judah became the father of Perez and Zerah by Tamar. Perez became the father of Hezron. Hezron became the father of Ram.")

        // John 1
        add("WEB", "John", 1, 1, "In the beginning was the Word, and the Word was with God, and the Word was God.")
        add("WEB", "John", 1, 2, "The same was in the beginning with God.")
        add("WEB", "John", 1, 3, "All things were made through him. Without him, nothing was made that has been made.")
        add("WEB", "John", 1, 4, "In him was life, and the life was the light of men.")
        add("WEB", "John", 1, 5, "The light shines in the darkness, and the darkness hasn't overcome it.")

        // Romans 8
        add("WEB", "Romans", 8, 1, "There is therefore now no condemnation to those who are in Christ Jesus, who don't walk according to the flesh, but according to the Spirit.")
        add("WEB", "Romans", 8, 2, "For the law of the Spirit of life in Christ Jesus made me free from the law of sin and of death.")
        add("WEB", "Romans", 8, 3, "For what the law couldn't do, in that it was weak through the flesh, God did, sending his own Son in the likeness of sinful flesh and for sin, he condemned sin in the flesh;")
        add("WEB", "Romans", 8, 4, "that the ordinance of the law might be fulfilled in us, who walk not after the flesh, but after the Spirit.")

        return verses
    }
}
