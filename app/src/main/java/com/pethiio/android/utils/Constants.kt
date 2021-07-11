package com.pethiio.android.utils

import java.text.SimpleDateFormat

class Constants {

    companion object {

        const val lookUpAnimals: String = "animals"
        const val lookUpGender: String = "gender"
        const val lookUpColor: String = "color"
        const val lookUpPurpose: String = "purpose"
        const val lookUpLanguage: String = "language"
        const val lookUpType: String = "type"

        const val notificationNewMesage: String = "NEW_MESSAGE"
        const val notificationNewMatch: String = "NEW_MATCH"
        const val notificationPromotional: String = "PROMOTIONAL"

        const val notificationNewMesageTitle: String = "newMessageTitle"
        const val notificationNewMatchTitle: String = "newMatchTitle"
        const val notificationPromotionalTitle: String = "promotionalTitle"


        val datePickerFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

        const val BASE_URL: String = "http://api.pawtind.com/api/"
        const val SOCKET_URL: String = "http://api.pawtind.com:9092/chat?token="
        const val SOCKET_TOPIC: String = "/topic/private.chat."
        const val SOCKET_PRIVATE_CHAT: String = "/app/private.chat"

        const val registerTitle: String = "title"
        const val registerEmailPlaceholder: String = "emailPlaceholder"
        const val registerEmailTitle: String = "emailTitle"
        const val registerForgotPasswordButtonTitle: String = "forgotPasswordButtonTitle"
        const val registerLoginButtonTitle: String = "loginButtonTitle"
        const val registerPasswordPlaceholder: String = "passwordPlaceholder"
        const val registerPasswordTitle: String = "passwordTitle"


        const val registerNameTitle: String = "nameTitle"
        const val registerSubTitle: String = "subTitle"
        const val registerNamePlaceholder: String = "namePlaceholder"
        const val registerSurnameTitle: String = "surnameTitle"
        const val registerSurnamePlaceholder: String = "surnamePlaceholder"
        const val registerPasswordDetail: String = "passwordDetail"
        const val registerTermsLinkTitle: String = "termsLinkTitle"
        const val registerTermsLink: String = "termsLink"
        const val registerTermsAndTitle: String = "termsAndTitle"
        const val registerPrivacyLinkTitle: String = "privacyLinkTitle"
        const val registerPrivacyLink: String = "privacyLink"
        const val registerPrivacyApproveTitle: String = "privacyApproveTitle"
        const val registerButtonTitle: String = "registerButtonTitle"
        const val registerDateOfBirthTitle: String = "dateOfBirthTitle"
        const val registerDateOfBirthPlaceholder: String = "dateOfBirthPlaceholder"
        const val registerGenderTitle: String = "genderTitle"
        const val registerAboutMe: String = "aboutMe"
        const val registerAboutMePlaceholder: String = "aboutMePlaceholder"
        const val registerAnimalInsertButtonTitle: String = "petInsertButtonTitle"
        const val registerSkipButtonTitle: String = "skipButtonTitle"
        const val registerCompleteButtonTitle: String = "completeButtonTitle"
        const val animalAddYearTitle: String = "yearTitle"
        const val animalAddYearPlaceholder: String = "yearPlaceholder"
        const val animalAddMonthTitle: String = "monthTitle"
        const val animalAddGenderTitle: String = "genderTitle"
        const val animalAddTypeTitle: String = "typeTitle"
        const val animalAddBreedTitle: String = "breedTitle"
        const val animalAddColorTitle: String = "colorTitle"
        const val animalAddCharacterTitle: String = "characterTitle"
        const val animalAddNextButtonTitle: String = "nextButtonTitle"
        const val animalAddMonthPlaceholder: String = "monthPlaceholder"

        const val animalListAddNewTitle: String = "addNewTitle"
        const val animalListCompleteButtonTitle: String = "completeButtonTitle"
        const val animalListHeader: String = "header"
        const val animalListSubTitle: String = "subTitle"
        const val animalListTitle: String = "title"

        const val petaboutTitle: String = "aboutTitle"
        const val petaboutPlaceholder: String = "aboutPlaceholder"


        const val petSearchDetailAboutOwnerTitle: String = "aboutOwnerTitle"
        const val petSearchDetailAboutOwnerAgeTitle: String = "ownerAgeTitle"
        const val petSearchDetailAboutColorTitle: String = "colorTitle"
        const val petSearchDetailDelete: String = "delete"
        const val petSearchDetailDetailTitle: String = "detailTitle"
        const val petSearchDetailListTypeTitle: String = "listTypeTitle"
        const val petSearchDetailOwner: String = "owner"
        const val petSearchDetailReport: String = "report"
        const val petSearchDetailUpdate: String = "update"
        const val petDeleteAlert: String = "deleteAlert"


        const val petSearchFilterDistanceTitle: String = "distanceTitle"
        const val petSearchFilterAgeTitle: String = "ageTitle"
        const val petSearchFilterGenderTitle: String = "genderTitle"
        const val petSearchFilterPurposeTitle: String = "purposeTitle"
        const val petSearchFilterAnimalTitle: String = "animalTitle"
        const val petSearchFilterButton: String = "filterButton"
        const val petSearchFilterCleanButton: String = "cleanButton"

        //region Pet Search

        const val petSearchEmptyMessageTitle: String = "emptyMessageTitle"
        const val petSearchTitle: String = "title"


        //endregion

        //region Report
        const val reportDetailEmptyError: String = "detailEmtpyError"
        const val reportDetailPlaceholder: String = "detailPlaceholder"
        const val reportDetailTitle: String = "detailTitle"
        const val reportButton: String = "reportButton"
        const val reportSubTitle: String = "subTitle"
        const val reportTitle: String = "title"
        const val reportTypeTitle: String = "typeTitle"

        //endregion


        //region Errors

        const val nameEmptyEror: String = "nameEmtpyError"
        const val surnameEmtpyError: String = "surnameEmtpyError"
        const val emailEmtpyError: String = "emailEmtpyError"
        const val passwordEmtpyError: String = "passwordEmtpyError"
        const val dateOfBirthEmtpyError: String = "dateOfBirthEmtpyError"
        const val genderEmtpyError: String = "genderEmtpyError"
        const val imageEmtpyError: String = "imageEmtpyError"
        const val aboutMeEmtpyError: String = "aboutMeEmtpyError"
        const val yearEmtpyError: String = "yearEmtpyError"
        const val monthEmtpyError: String = "monthEmtpyError"
        const val typeEmtpyError: String = "typeEmtpyError"
        const val breedEmtpyError: String = "breedEmtpyError"
        const val colorEmtpyError: String = "colorEmtpyError"
        const val aboutEmtpyError: String = "aboutEmtpyError"
        const val currentPasswordEmtpyError: String = "currentPasswordEmtpyError"
        const val newPasswordEmtpyError: String = "newPasswordEmtpyError"
        const val detailEmtpyError: String = "detailEmtpyError"

        //endregion

        //region Settings
        const val supportDetailButton: String = "supportDetailButton"
        const val changePasswordTitle: String = "changePasswordTitle"
        const val notificationTitle: String = "notificationTitle"
        const val supportTitle: String = "supportTitle"
        const val aboutAppTitle: String = "aboutAppTitle"
        const val logoutTitle: String = "logoutTitle"
        //endregion

        //region ChangePass
        const val currentPasswordTitle: String = "currentPasswordTitle"
        const val currentPasswordPlaceholder: String = "currentPasswordPlaceholder"
        const val newPasswordTitle: String = "newPasswordTitle"
        const val newPasswordPlaceholder: String = "newPasswordPlaceholder"
        const val newPasswordDetail: String = "newPasswordDetail"
        const val passwordTitle: String = "passwordTitle"
        const val passwordPlaceholder: String = "passwordPlaceholder"
        const val changeButtonTitle: String = "changeButtonTitle"

        //endregion

        //region Chat

        const val emptyMessageTitle: String = "emptyMessageTitle"
        const val inputPlaceholder: String = "inputPlaceholder"

        //endregion

        //region Location
        const val locationButton: String = "locationButton"
        const val nextButton: String = "nextButton"

        //endregion

        //region Reset Pass
        const val sendButtonTitle: String = "sendButtonTitle"
        const val pinPlaceholder: String = "pinPlaceholder"
        const val pinSubTitleForEmail: String = "subTitleForEmail"
        const val pinEmtpyError: String = "pinEmtpyError"
        const val nextButtonTitle: String = "nextButtonTitle"


        //endregion

    }

}