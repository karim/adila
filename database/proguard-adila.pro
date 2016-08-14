# ProGuard for Adila database.
#
# All classes in the database are called via reflection, and since they are not being called
# directly, ProGuard will assume they are unused and will remove them all.
#
# If you are using ProGuard with your application, you must use this file to prevent ProGuard from
# removing all classes from the database. Even if you are not using ProGuard, you can use this file
# to remove any field you are not using in your application to shrink the database size.
#
# At minimum, you must include "-keep final class adila.db.* {}" to keep all classes from being
# removed. Then list one or more fields from the classes to keep. By default, this file will list
# all fields so you can have the full database. Then, you are free to remove any field you are not
# using in your application.

# Keep all the classes from the database, and all their fields.
-keep final class adila.db.* {
    public static final java.lang.String MANUFACTURER;

    public static final java.lang.String NAME;

    public static final java.lang.String SERIES;
}
