package com.example.model

enum class ThemeCategory(val titleEn: String, val titleAr: String) {
    CORE("Core", "الأساسية"),
    CLASSIC_DARK("Developer Dark", "ثيمات المطورين الليلية"),
    CLASSIC_LIGHT("Developer Light", "ثيمات المطورين النهارية"),
    LIQUID_GLASS("Liquid Glass", "الزجاج السائل الفاخر")
}

/**
 * Multi-Theme Engine modes supported across Data Pulse / DNS Pulse:
 * Includes Core System/Neumorphic/Glass engines plus 60 curated Developer & Liquid Glass themes.
 */
enum class AppThemeMode(
    val titleEn: String,
    val titleAr: String,
    val descEn: String,
    val descAr: String,
    val isDark: Boolean = true,
    val category: ThemeCategory = ThemeCategory.CLASSIC_DARK
) {
    // Core Built-in Engines (7)
    SYSTEM(
        titleEn = "Follow System",
        titleAr = "اتبع الهاتف",
        descEn = "Dynamic Auto Switch",
        descAr = "تبديل تلقائي نهاري وليلي",
        isDark = false,
        category = ThemeCategory.CORE
    ),
    LIGHT_MODE(
        titleEn = "Pearl Glass",
        titleAr = "لؤلؤي بلاتيني",
        descEn = "Warm Pearl Glass & Obsidian",
        descAr = "زجاج لؤلؤي دافئ ونصوص فحمية",
        isDark = false,
        category = ThemeCategory.CORE
    ),
    NEUMORPHISM(
        titleEn = "Neumorphism Light",
        titleAr = "نيومورفيزم نهاري",
        descEn = "Soft Ice-Gray Tactile UI",
        descAr = "تصميم ثلاثي الأبعاد نهاري",
        isDark = false,
        category = ThemeCategory.CORE
    ),
    NEUMORPHISM_DARK(
        titleEn = "Neumorphism Dark",
        titleAr = "نيومورفيزم ليلي",
        descEn = "Matte Slate 3D Soft UI",
        descAr = "تصميم ثلاثي الأبعاد ليلي",
        isDark = true,
        category = ThemeCategory.CORE
    ),
    LIQUID_GLASS_DEFAULT(
        titleEn = "Liquid Glass",
        titleAr = "الزجاج السائل",
        descEn = "Deep Obsidian & Blue",
        descAr = "زجاج أوبسيديان وأزرق داكن",
        isDark = true,
        category = ThemeCategory.CORE
    ),
    LIQUID_NEON_CYAN(
        titleEn = "Neon Cyan",
        titleAr = "نيون سيان",
        descEn = "Electric Cyan & Teal",
        descAr = "سيان كهربائي وزمردي مشع",
        isDark = true,
        category = ThemeCategory.CORE
    ),
    AMOLED_PITCH_BLACK(
        titleEn = "AMOLED Black",
        titleAr = "سواد نقي AMOLED",
        descEn = "Pure Black #000000",
        descAr = "أسود نقي لتوفير البطارية",
        isDark = true,
        category = ThemeCategory.CORE
    ),

    // 40 Classic Developer & Editor Themes (1..40)
    MONOKAI("Monokai", "مونوكاي", "Iconic Warm Charcoal & Lime", "مونوكاي الكلاسيكي الدافئ", true, ThemeCategory.CLASSIC_DARK),
    MONOKAI_PRO("Monokai Pro", "مونوكاي برو", "Smooth Graphite & Gold", "جرافيت ناعم ولمسات ذهبية", true, ThemeCategory.CLASSIC_DARK),
    SOLARIZED_DARK("Solarized Dark", "سولارايزد ليلي", "Deep Teal Slate & Azure", "أردوازي مخضر داكن", true, ThemeCategory.CLASSIC_DARK),
    SOLARIZED_LIGHT("Solarized Light", "سولارايزد نهاري", "Warm Cream Paper & Cyan", "ورقي دافئ مريح للعين", false, ThemeCategory.CLASSIC_LIGHT),
    ONE_DARK_PRO("One Dark Pro", "ون دارك برو", "Balanced Slate & Sky Blue", "داكن متوازن مع أزرق سماوي", true, ThemeCategory.CLASSIC_DARK),
    ONE_LIGHT("One Light", "ون لايت", "Crisp Bright Paper & Cobalt", "نهاري نقي وأزرق كوبالت", false, ThemeCategory.CLASSIC_LIGHT),
    DRACULA("Dracula", "دراكولا", "Vampire Purple & Pink Glow", "بنفسجي دراكولا ووردي مشع", true, ThemeCategory.CLASSIC_DARK),
    TOMORROW_NIGHT_EIGHTIES("Tomorrow Night Eighties", "تومورو نايت 80s", "Retro Charcoal & Soft Blue", "رمادي كلاسيكي وأزرق هادئ", true, ThemeCategory.CLASSIC_DARK),
    NORD("Nord", "نورد", "Arctic Frosted Polar Blue", "أزرق قطبي متجمد", true, ThemeCategory.CLASSIC_DARK),
    NIGHT_OWL("Night Owl", "نايت أول", "Midnight Navy & Mint", "كحلي ليلي مع نعناعي", true, ThemeCategory.CLASSIC_DARK),
    BLULOCO_DARK("Bluloco Dark", "بلولوكو ليلي", "Deep Slate & Neon Emerald", "داكن حديث بزمردي مشع", true, ThemeCategory.CLASSIC_DARK),
    AYU_DARK("Ayu Dark", "آيو ليلي", "Obsidian Abyss & Amber Gold", "سواد عميق وذهبي كهرماني", true, ThemeCategory.CLASSIC_DARK),
    AYU_MIRAGE("Ayu Mirage", "آيو ميراج", "Twilight Navy & Honey Gold", "كحلي غسقي وعسلي دافئ", true, ThemeCategory.CLASSIC_DARK),
    AYU_LIGHT("Ayu Light", "آيو نهاري", "Clean Alabaster & Tangerine", "أبيض ناعم وبرتقالي حيوي", false, ThemeCategory.CLASSIC_LIGHT),
    WINTER_IS_COMING("Winter is Coming", "وينتر إز كامينغ", "Frost Navy & Ice Cyan", "شتوي كحلي وسيان جليدي", true, ThemeCategory.CLASSIC_DARK),
    ZENBURN("Zenburn", "زينبيرن", "Low-Contrast Warm Charcoal", "رمادي دافئ مريح للعين", true, ThemeCategory.CLASSIC_DARK),
    SYNTHWAVE_84("SynthWave '84", "سينث ويف 84", "Retro Neon Magenta & Cyan", "ريترو نيون وردي وسيان", true, ThemeCategory.CLASSIC_DARK),
    SHADES_OF_PURPLE("Shades of Purple", "ظلال البنفسجي", "Royal Indigo & Electric Gold", "أرجواني ملكي وذهبي ساطع", true, ThemeCategory.CLASSIC_DARK),
    LASER_WAVE("LaserWave", "ليزر ويف", "80s Neon Rose & Aqua", "نيون ليزر وردي وأزرق", true, ThemeCategory.CLASSIC_DARK),
    ABYSS("Abyss", "الهاوية Abyss", "Deep Trench Midnight Blue", "أزرق أعماق المحيط الليلي", true, ThemeCategory.CLASSIC_DARK),
    COBALT2("Cobalt2", "كوبالت 2", "Rich Cobalt Blue & Gold", "أزرق كوبالت غني وذهبي", true, ThemeCategory.CLASSIC_DARK),
    RED("Red", "الأحمر Red", "Deep Crimson & Flame Coral", "أحمر قرمزي داكن وناري", true, ThemeCategory.CLASSIC_DARK),
    POIMANDRES("Poimandres", "بويماندرس", "Minimal Midnight & Mint Teal", "داكن أنيق ونعناعي ناعم", true, ThemeCategory.CLASSIC_DARK),
    HOPSCOTCH("Hopscotch", "هوبسكوتش", "Plum Charcoal & Vivid Cyan", "داكن أرجواني وسيان ساطع", true, ThemeCategory.CLASSIC_DARK),
    GRUVBOX_DARK("Gruvbox Dark", "جروف بوكس ليلي", "Retro Espresso & Warm Ochre", "إسبريسو دافئ وأصفر عتيق", true, ThemeCategory.CLASSIC_DARK),
    GRUVBOX_LIGHT("Gruvbox Light", "جروف بوكس نهاري", "Vintage Parchment & Amber", "رق ورقي دافئ وكهرماني", false, ThemeCategory.CLASSIC_LIGHT),
    OCEANIC_NEXT("Oceanic Next", "أوشيانيك نيكست", "Deep Sea Slate & Aqua", "أردوازي بحري وأكوا ناعم", true, ThemeCategory.CLASSIC_DARK),
    BASE16_EIGHTIES("Base16 Eighties", "بيس 16 الثمانينات", "Matte Graphite & Teal Blue", "جرافيت مطفي وأزرق مخضر", true, ThemeCategory.CLASSIC_DARK),
    TOMORROW_NIGHT_BLUE("Tomorrow Night Blue", "تومورو نايت أزرق", "Classic Royal Navy & Ice", "أزرق بحري كلاسيكي وجليدي", true, ThemeCategory.CLASSIC_DARK),
    MATERIAL_OCEANIC("Material Theme (Oceanic)", "ماتيريال أوشيانيك", "Material Teal Slate & Mint", "ماتيريال بحري ونعناعي", true, ThemeCategory.CLASSIC_DARK),
    MATERIAL_PALENIGHT("Material Theme (Palenight)", "ماتيريال بيل نايت", "Soft Lavender Dark Slate", "داكن أرجواني لافندر ناعم", true, ThemeCategory.CLASSIC_DARK),
    MATERIAL_DARKER("Material Theme (Darker)", "ماتيريال داكن", "Deep Carbon & Amber Glow", "كربوني داكن وتوهج كهرماني", true, ThemeCategory.CLASSIC_DARK),
    CATPPUCCIN_MOCHA("Catppuccin (Mocha)", "كاتبوتشين موكا", "Pastel Lavender & Sapphire", "موكا باستيل ولافندر ناعم", true, ThemeCategory.CLASSIC_DARK),
    KANAGAWA("Kanagawa", "كاناغاوا", "Sumi Ink & Wave Blue", "حبر ياباني وأزرق الموج", true, ThemeCategory.CLASSIC_DARK),
    ROSE_PINE("Rose Pine", "روز باين", "Velvet Pine & Dawn Rose", "داكن مخملي ووردي هادئ", true, ThemeCategory.CLASSIC_DARK),
    GITHUB_DARK("GitHub Dark", "جيت هب ليلي", "GitHub Graphite & Azure", "جيت هب داكن وأزرق سماوي", true, ThemeCategory.CLASSIC_DARK),
    GITHUB_LIGHT("GitHub Light", "جيت هب نهاري", "Clean Primer White & Blue", "جيت هب نهاري نقي وأزرق", false, ThemeCategory.CLASSIC_LIGHT),
    PANDA_THEME("Panda Theme", "ثيم باندا", "Superminimal Charcoal & Neon Pink", "فحمي بسيط ووردي نيون", true, ThemeCategory.CLASSIC_DARK),
    ATOM_ONE_DARK("Atom One Dark", "أتوم ون دارك", "Classic Atom Slate & Cyan", "أتوم داكن وسيان نقي", true, ThemeCategory.CLASSIC_DARK),
    TOKYO_NIGHT("Tokyo Night", "ليلة طوكيو", "Tokyo Skyline Indigo & Blue", "أضواء طوكيو الليلية الزرقاء", true, ThemeCategory.CLASSIC_DARK),

    // 20 Bespoke Liquid Glass Themes (41..60)
    OBSIDIAN_LIQUID_GLASS("Obsidian Liquid Glass", "زجاج أوبسيديان السائل", "Pitch Obsidian & Cyan Refraction", "أوبسيديان عميق وانعكاس سيان", true, ThemeCategory.LIQUID_GLASS),
    FROSTED_ARCTIC_GLASS("Frosted Arctic Glass", "زجاج قطبي متجمد", "Icy Frosted Translucent Blue", "زجاج جليدي فاتح وأزرق قطبي", false, ThemeCategory.LIQUID_GLASS),
    CYBER_NEON_LIQUID("Cyber Neon Liquid", "زجاج سايبر نيون السائل", "Hyper Neon Mint & Magenta", "سايبر نيون زمردي وأرجواني", true, ThemeCategory.LIQUID_GLASS),
    SMOKED_QUARTZ_GLASS("Smoked Quartz Glass", "زجاج كوارتز مدخن", "Smoked Charcoal & Champagne Gold", "كوارتز مدخن وذهب شامبانيا", true, ThemeCategory.LIQUID_GLASS),
    MIDNIGHT_VELVET_GLASS("Midnight Velvet Glass", "زجاج مخمل منتصف الليل", "Velvet Plum Glass & Rose Glow", "مخمل ليلي وبريق بنفسجي", true, ThemeCategory.LIQUID_GLASS),
    AURORA_BOREALIS_GLASS("Aurora Borealis Glass", "زجاج الشفق القطبي", "Northern Lights Emerald & Sky", "شفق قطبي زمردي وسماوي", true, ThemeCategory.LIQUID_GLASS),
    CRYSTAL_PRISM_LIQUID("Crystal Prism Liquid", "زجاج المنشور البلوري", "Prismatic Frosted Lavender Light", "منشور بلوري فاتح ولافندر", false, ThemeCategory.LIQUID_GLASS),
    DEEP_OCEAN_GLASS("Deep Ocean Glass", "زجاج أعماق المحيط", "Abyssal Aqua & Electric Wave", "محيط عميق وأمواج سيان", true, ThemeCategory.LIQUID_GLASS),
    AMBER_MERCURY_GLASS("Amber Mercury Glass", "زجاج الزئبق الكهرماني", "Molten Amber & Dark Bronze", "كهرمان منصهر وبرونز داكن", true, ThemeCategory.LIQUID_GLASS),
    TITANIUM_MIST_GLASS("Titanium Mist Glass", "زجاج ضباب التيتانيوم", "Brushed Titanium Light Frost", "ضباب تيتانيوم فاتح أنيق", false, ThemeCategory.LIQUID_GLASS),
    HOLOGRAPHIC_LIQUID_GLASS("Holographic Liquid Glass", "زجاج هولوغرافي سائل", "Iridescent Prism Cyan & Violet", "هولوغرافي متقزح سيان وأرجواني", true, ThemeCategory.LIQUID_GLASS),
    CARBON_BLUR_GLASS("Carbon Blur Glass", "زجاج كربون ضبابي", "Stealth Carbon & Brushed Ice", "كربون شبح وضباب جليدي", true, ThemeCategory.LIQUID_GLASS),
    EMERALD_TRANSLUCENT_GLASS("Emerald Translucent Glass", "زجاج زمردي شفاف", "Deep Jade Glass & Mint Pulse", "زمرد شفاف ونبض نعناعي", true, ThemeCategory.LIQUID_GLASS),
    ULTRAVIOLET_FROST_GLASS("Ultraviolet Frost Glass", "زجاج صقيع فوق البنفسجي", "Ultraviolet Glass & Neon Frost", "صقيع فوق بنفسجي وسماوي", true, ThemeCategory.LIQUID_GLASS),
    SAPPHIRE_LIQUID_GLASS("Sapphire Liquid Glass", "زجاج الياقوت السائل", "Royal Sapphire & Ice Azure", "ياقوت ملكي وأزرق جليدي", true, ThemeCategory.LIQUID_GLASS),
    PEARLESCENT_DARK_GLASS("Pearlescent Dark Glass", "زجاج لؤلؤي داكن", "Dark Pearl Sheen & Silver Mist", "لؤلؤ داكن وبريق فضي", true, ThemeCategory.LIQUID_GLASS),
    RUBY_REFRACTIVE_GLASS("Ruby Refractive Glass", "زجاج الياقوت الأحمر الانكساري", "Refractive Ruby & Crimson Glow", "ياقوت أحمر انكساري قرمزي", true, ThemeCategory.LIQUID_GLASS),
    CHROMATIC_ABERRATION_GLASS("Chromatic Aberration Glass", "زجاج الانزياح اللوني", "Dual Magenta-Cyan Spectral Split", "طيف لوني مزدوج وردي وسيان", true, ThemeCategory.LIQUID_GLASS),
    LIQUID_MERCURY_DARK("Liquid Mercury Dark", "الزئبق السائل الداكن", "Chrome Liquid Sheen & Cool Silver", "زئبق كروم داكن وفضي نقي", true, ThemeCategory.LIQUID_GLASS),
    BIOLUMINESCENT_GLASS("Bioluminescent Glass", "الزجاج الحيوي المضيء", "Deep Reef Bio-Cyan & Aqua Glow", "إضاءة بحرية حيوية وسيان مشع", true, ThemeCategory.LIQUID_GLASS);

    fun getDisplayName(lang: AppLanguage): String =
        if (lang == AppLanguage.ARABIC) titleAr else titleEn

    fun getDescription(lang: AppLanguage): String =
        if (lang == AppLanguage.ARABIC) descAr else descEn
}

typealias AppTheme = AppThemeMode
