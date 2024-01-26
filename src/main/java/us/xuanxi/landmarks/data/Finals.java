package us.xuanxi.landmarks.data;

public class Finals {
    public static final String config_path_prefix = "landmarks.";
    public static final String config_path_with_location_world = ".location.world";
    public static final String config_path_with_location_x = ".location.x";
    public static final String config_path_with_location_z = ".location.y";
    public static final String config_path_with_location_y = ".location.z";
    public static final String config_path_with_location_yaw = ".location.yaw";
    public static final String config_path_with_location_pitch = ".location.pitch";
    public static final String config_path_with_creator = ".creator";

    public static final String command_prefix = "landmarks";
    public static final String command_go = "go";
    public static final String command_new = "new";
    public static final String command_ls = "ls";
    public static final String command_reload = "reload";
    public static final String command_rm = "rm";

    public static final String permission_command_go = "landmarks.go";
    public static final String permission_command_new = "landmarks.new";
    public static final String permission_command_ls = "landmarks.ls";
    public static final String permission_command_rm = "landmarks.rm";
    public static final String permission_command_reload = "landmarks.reload";
    public static final String permission_admin = "landmarks.admin";

    public static final String msg_no_permission = "You do not have permission to use this command!";
    public static final String msg_not_player = "You must be a player to use this command!";
    public static final String msg_landmark_not_exist = "Landmark does not exist: ";
    public static final String msg_landmark_created = "Created a new landmark: ";
    public static final String msg_landmark_updated = "Updated landmark: ";
    public static final String msg_landmark_removed = "Removed landmark: ";
    public static final String msg_plugin_reload = "Landmarks Plugin reloaded!";
    public static final String msg_teleported_to_landmark = "Teleported to landmark: ";
}
