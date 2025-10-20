#![cfg_attr(windows, windows_subsystem = "windows")]

use std::env;
use std::process::Command;

fn main() {
    if let Ok(exe_path) = env::current_exe() {
        if let Some(exe_dir) = exe_path.parent() {
            let _ = env::set_current_dir(exe_dir);
        }
    }

    let _ = Command::new("openjdk\\bin\\javaw.exe")
        .arg("-jar")
        .arg("star-dot-box_deploy.jar")
        .spawn();
}
