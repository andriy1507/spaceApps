package com.spaceapps.myapplication.features.onBoarding

typealias OnActionSubmit = (OnBoardingAction) -> Unit

sealed class OnBoardingAction {
    object ContinueClick : OnBoardingAction()
}
