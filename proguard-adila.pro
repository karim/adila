# ProGuard for Adila database.
#
# All classes in the database are called via reflection, and since they are not being called
# directly, ProGuard will assume they are unused and will remove them all.
#
# If you are using ProGuard with your application, you must use this file to prevent ProGuard from
# removing all classes from the database.

# Keep all the classes from the database, and all their fields.
-keep final class adila.db.* {
    public static final java.lang.String DATA;
}
