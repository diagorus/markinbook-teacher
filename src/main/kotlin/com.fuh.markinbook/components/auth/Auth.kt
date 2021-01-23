package com.fuh.markinbook.components.auth

import com.ccfraser.muirwik.components.*
import com.fuh.markinbook.data.*
import kotlinx.coroutines.launch
import kotlinx.css.*
import react.*
import react.router.dom.useHistory
import styled.css
import styled.styledDiv

external interface AuthProps : RProps {
    var showLoading: (show: Boolean) -> Unit
}

val auth = functionalComponent<AuthProps> { props ->
    styledDiv {
        css {
            padding(2.spacingUnits)
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = Align.center
        }
        mPaper {
            css {
                padding(4.spacingUnits)
                display = Display.flex
                flexDirection = FlexDirection.column
            }

            val (selectedTab, setSelectedTab) = useState(AuthTab.LOGIN)

            mTabs(
                selectedTab,
                textColor = MTabTextColor.primary,
                onChange = { _, value ->
                    setSelectedTab(value as AuthTab)
                }
            ) {
                mTab("Ввійти", AuthTab.LOGIN)
                mTab("Зареєструватися", AuthTab.REGISTER)
            }

            val (email, setEmail) = useState("")
            val (emailError, setEmailError) = useState<String?>(null)

            val (password, setPassword) = useState("")
            val (passwordError, setPasswordError) = useState<String?>(null)

            val (repeatPassword, setRepeatPassword) = useState("")
            val (repeatPasswordError, setRepeatPasswordError) = useState<String?>(null)

            val (firstName, setFirstName) = useState("")
            val (firstNameError, setFirstNameError) = useState<String?>(null)


            val (lastName, setLastName) = useState("")
            val (lastNameError, setLastNameError) = useState<String?>(null)

            val (school, setSchool) = useState<School?>(null)
            val (schoolError, setSchoolError) = useState(false)

            val history = useHistory()

            when (selectedTab) {
                AuthTab.LOGIN -> {
                    login {
                        this.email = email
                        this.emailError = emailError
                        onEmailChanged = {
                            setEmailError(null)
                            setEmail(it)
                        }

                        this.password = password
                        this.passwordError = passwordError
                        onPasswordChanged = {
                            setPasswordError(null)
                            setPassword(it)
                        }

                        onLoginClick = {
                            var valid = true

                            if (email.isEmpty()) {
                                setEmailError("Обов'язкове поле!")
                                valid = false
                            }
                            if (password.isEmpty()) {
                                setPasswordError("Обов'язкове поле!")
                                valid = false
                            }

                            if (valid) {
                                appScope.launch {
                                    props.showLoading(true)
                                    try {
                                        val token = ApiRepository.signIn(email, password)
                                        props.showLoading(false)
                                        SessionManager.token = token
                                        history.push("/schedule")
                                    } catch (e: Exception) {
                                        props.showLoading(false)
                                        //todo
                                    }
                                }
                            }
                        }
                    }
                }
                AuthTab.REGISTER -> {
                    register {
                        this.email = email
                        this.emailError = emailError
                        onEmailChanged = {
                            setEmailError(null)
                            setEmail(it)
                        }

                        this.password = password
                        this.passwordError = passwordError
                        onPasswordChanged = {
                            setPasswordError(null)
                            setPassword(it)
                        }

                        this.repeatPassword = repeatPassword
                        this.repeatPasswordError = repeatPasswordError
                        onRepeatPasswordChanged = {
                            setRepeatPasswordError(null)
                            setRepeatPassword(it)
                        }

                        this.firstName = firstName
                        this.firstNameError = firstNameError
                        onFirstNameChanged = {
                            setFirstNameError(null)
                            setFirstName(it)
                        }

                        this.lastName = lastName
                        this.lastNameError = lastNameError
                        onLastNameChanged = {
                            setLastNameError(null)
                            setLastName(it)
                        }

                        this.lastName = lastName
                        this.lastNameError = lastNameError
                        onLastNameChanged = {
                            setLastNameError(null)
                            setLastName(it)
                        }

                        this.school = school
                        this.schoolError = schoolError
                        onSchoolChosen = {
                            setSchoolError(false)
                            setSchool(it)
                        }

                        onRegisterClick = {
                            var valid = true

                            if (email.isEmpty()) {
                                valid = false
                                setEmailError("Обов'язкове поле!")
                            }
                            if (password.isEmpty()) {
                                valid = false
                                setPasswordError("Обов'язкове поле!")
                            }
                            when {
                                repeatPassword.isEmpty() -> {
                                    valid = false
                                    setRepeatPasswordError("Обов'язкове поле!")
                                }
                                password != repeatPassword -> {
                                    valid = false
                                    setRepeatPasswordError("Пароль не співпадає!")
                                }
                            }
                            if (firstName.isEmpty()) {
                                valid = false
                                setFirstNameError("Обов'язкове поле!")
                            }
                            if (lastName.isEmpty()) {
                                valid = false
                                setLastNameError("Обов'язкове поле!")
                            }
                            if (school == null) {
                                valid = false
                                setSchoolError(true)
                            }

                            if (valid) {
                                appScope.launch {
                                    props.showLoading(true)
                                    try {
                                        val token = ApiRepository.signUp(email, password, firstName, lastName, school?.id)
                                        props.showLoading(false)
                                        SessionManager.token = token
                                        history.push("/schedule")
                                    } catch (e: Exception) {
                                        //todo
                                        props.showLoading(false)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class AuthTab {
    LOGIN,
    REGISTER
}

fun RBuilder.auth(handler: AuthProps.() -> Unit): ReactElement {
    return child(auth) {
        attrs(handler)
    }
}