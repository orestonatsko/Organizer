package ua.com.info.common.menu

import java.util.ArrayList

/**
 * Створено v.m 31.03.2015.
 */

class MenuDescription {

    var groups = ArrayList<MenuGroup>()

    class MenuGroup {
        var name: String? = null
        val subItems = ArrayList<MenuItem>()
    }

    fun add(Group: String, item: MenuItem): MenuItem {
        var group: MenuGroup? = null
        for (g in groups) {
            if (g.name == Group) {
                group = g
                break
            }
        }
        if (group == null) {
            group = MenuGroup()
            group.name = Group
            groups.add(group)
        }
        item.name = Group
        group.subItems.add(item)
        return item
    }

    fun add(group: String, activityClass: Class<*>): MenuItem {
        val item = MenuItem(activityClass)
        return add(group, item)
    }

}
