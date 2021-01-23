package com.fuh.markinbook

import com.ccfraser.muirwik.components.*
import com.fuh.markinbook.components.auth.auth
import com.fuh.markinbook.components.index
import com.fuh.markinbook.components.schedule
import kotlinx.css.*
import react.*
import react.router.dom.hashRouter
import react.router.dom.route
import react.router.dom.switch
import styled.css
import styled.styledDiv

interface IdProps : RProps {
    var id: Int
}

val app = functionalComponent<RProps> {
    styledDiv {
        mAppBar(position = MAppBarPosition.sticky) {
            mToolbar(variant = ToolbarVariant.regular) {
                mTypography("Markinbook", variant = MTypographyVariant.h6, color = MTypographyColor.inherit)
            }
        }
    }

    val (loading, setLoading) = useState(false)

    styledDiv {
        css {
            paddingTop = 2.spacingUnits
        }

        hashRouter {
            switch {
                route("/", exact = true) {
                    index()
                }
                route("/schedule") {
                    schedule {
                        showLoading = {
                            setLoading(it)
                        }
                    }
                }
                route("/add-lesson") {
                    addLesson()
                }
                route("/auth") {
                    auth {
                        showLoading = {
                            setLoading(it)
                        }
                    }
                }
            }
        }
    }

    mBackdrop(loading) {
        css {
            zIndex = 100
            color = Color("#fff")
        }
        mCircularProgress(color = MCircularProgressColor.inherit)
    }
}

fun RBuilder.app(): ReactElement {
    return child(app)
}