import { useState} from "react";
import useAuthStore from "../store/authStore";
import { useMe, useUpdateMe } from "../features/profile/hooks";
import { getErrorMessage } from "../utils/errorHandler";

const Profile = () => {
  const { data: user, isLoading } = useMe();
  const { mutate: updateMe, isPending: saving } = useUpdateMe();

  const [form, setForm] = useState({ userName: "", email: "" });
  const [editing, setEditing] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const logout = useAuthStore((state) => state.logout);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError("");
  };

  const handleSave = () => {
    if (!form.userName.trim() && !form.email.trim()) {
      setError("At least one field is required.");
      return;
    }

    const payload = {};
    if (form.userName.trim() !== user.userName) payload.userName = form.userName.trim();
    if (form.email.trim() !== user.email) payload.email = form.email.trim();

    if (Object.keys(payload).length === 0) {
      setEditing(false);
      return;
    }

    updateMe(payload, {
      onSuccess: () => {
        const usernameChanged = payload.userName !== undefined;
        
        if(usernameChanged){
          setSuccess("Username changed. Logging you out...");
          setTimeout(() => logout(), 2500);
        } else {
          setEditing(false);
          setError("");
          setSuccess("Profile updated successfully.");
          setTimeout(() => setSuccess(""), 3000);
        }
      },
      onError: (err) => {
        setError(getErrorMessage(err));
      },
    });
  };

  const handleCancel = () => {
    setEditing(false);
    setError("");
    if (user) setForm({ userName: user.userName, email: user.email });
  };

  const handleEditStart = () => {
    setForm({ userName: user.userName, email: user.email});
    setEditing(true);
  }

  const getRoleBadge = (role) => {
    const map = {
      ROLE_SUPER_ADMIN: "bg-purple-100 text-purple-700",
      ROLE_ADMIN: "bg-indigo-100 text-indigo-700",
      ROLE_USER: "bg-gray-100 text-gray-600",
    };
    return map[role] ?? "bg-gray-100 text-gray-600";
  };

  const getRoleLabel = (role) => {
    const map = {
      ROLE_SUPER_ADMIN: "Super Admin",
      ROLE_ADMIN: "Admin",
      ROLE_USER: "User",
    };
    return map[role] ?? role;
  };

  if (isLoading) {
    return (
      <div className="max-w-2xl mx-auto space-y-4">
        {[1, 2, 3].map((i) => (
          <div
            key={i}
            className="bg-white rounded-xl border border-gray-200 p-6 animate-pulse h-20"
          />
        ))}
      </div>
    );
  }

  return (
    <div className="max-w-2xl mx-auto">
      {/* header */}
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-gray-800">Profile</h1>
        <p className="text-gray-500 mt-1">View and update your account details.</p>
      </div>

      {success && (
        <div className="bg-green-50 text-green-600 text-sm rounded-lg p-3 mb-4">
          {success}
        </div>
      )}

      {/* profile card */}
      <div className="bg-white rounded-xl border border-gray-200 p-6 mb-4">
        {/* avatar + role */}
        <div className="flex items-center gap-4 mb-6 pb-6 border-b border-gray-100">
          <div className="w-14 h-14 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-600 text-xl font-bold shrink-0">
            {user?.userName?.charAt(0).toUpperCase()}
          </div>
          <div>
            <p className="font-semibold text-gray-800 text-lg">{user?.userName}</p>
            <span
              className={`text-xs font-medium px-2.5 py-1 rounded-full ${getRoleBadge(user?.role)}`}
            >
              {getRoleLabel(user?.role)}
            </span>
          </div>
        </div>

        {/* fields */}
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-500 mb-1">
              Username
            </label>
            {editing ? (
              <input
                type="text"
                name="userName"
                value={form.userName}
                onChange={handleChange}
                className="w-full border border-gray-300 rounded-lg px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
            ) : (
              <p className="text-sm text-gray-800 font-medium">{user?.userName}</p>
            )}
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-500 mb-1">
              Email
            </label>
            {editing ? (
              <input
                type="email"
                name="email"
                value={form.email}
                onChange={handleChange}
                className="w-full border border-gray-300 rounded-lg px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
            ) : (
              <p className="text-sm text-gray-800 font-medium">{user?.email}</p>
            )}
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-500 mb-1">
              Account ID
            </label>
            <p className="text-xs text-gray-400 font-mono">{user?.id}</p>
          </div>
        </div>

        {error && (
          <p className="text-red-500 text-xs mt-4">{error}</p>
        )}

        {/* actions */}
        <div className="mt-6 flex gap-3">
          {editing ? (
            <>
              <button
                onClick={handleSave}
                disabled={saving}
                className="bg-indigo-600 hover:bg-indigo-700 disabled:bg-indigo-300 text-white font-semibold px-5 py-2.5 rounded-lg text-sm transition-colors"
              >
                {saving ? "Saving..." : "Save Changes"}
              </button>
              <button
                onClick={handleCancel}
                className="bg-white hover:bg-gray-50 text-gray-600 font-semibold px-5 py-2.5 rounded-lg text-sm border border-gray-200 transition-colors"
              >
                Cancel
              </button>
            </>
          ) : (
            <button
              onClick={handleEditStart}
              className="bg-indigo-600 hover:bg-indigo-700 text-white font-semibold px-5 py-2.5 rounded-lg text-sm transition-colors"
            >
              Edit Profile
            </button>
          )}
        </div>
      </div>
    </div>
  );
};

export default Profile;