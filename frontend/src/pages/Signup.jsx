import { useState } from "react";
import { Link } from "react-router-dom";
import AuthLayout from "../layouts/AuthLayout";
import { useSignup } from "../features/auth/hooks";

const Signup = () => {
  const [form, setForm] = useState({ userName: "", email: "", password: "" });
  const [errors, setErrors] = useState({ fields: {}, general: "" });
  const { mutate: signup, isPending } = useSignup(setErrors);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setErrors((prev) => ({
      ...prev,
      fields: { ...prev.fields, [e.target.name]: "" },
      general: "",
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    signup(form);
  };

  return (
    <AuthLayout>
      <h2 className="text-2xl font-bold text-gray-800 mb-6">Create account</h2>

      {errors.general && (
        <div className="bg-red-50 text-red-600 text-sm rounded-lg p-3 mb-4">
          {errors.general}
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Username
          </label>
          <input
            type="text"
            name="userName"
            value={form.userName}
            onChange={handleChange}
            placeholder="Enter username"
            className={`w-full border rounded-lg px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 ${
              errors.fields.userName ? "border-red-400" : "border-gray-300"
            }`}
            required
          />
          {errors.fields.userName && (
            <p className="text-red-500 text-xs mt-1">{errors.fields.userName}</p>
          )}
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Email
          </label>
          <input
            type="email"
            name="email"
            value={form.email}
            onChange={handleChange}
            placeholder="Enter email"
            className={`w-full border rounded-lg px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 ${
              errors.fields.email ? "border-red-400" : "border-gray-300"
            }`}
            required
          />
          {errors.fields.email && (
            <p className="text-red-500 text-xs mt-1">{errors.fields.email}</p>
          )}
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Password
          </label>
          <input
            type="password"
            name="password"
            value={form.password}
            onChange={handleChange}
            placeholder="Enter password"
            className={`w-full border rounded-lg px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 ${
              errors.fields.password ? "border-red-400" : "border-gray-300"
            }`}
            required
          />
          {errors.fields.password && (
            <p className="text-red-500 text-xs mt-1">{errors.fields.password}</p>
          )}
        </div>

        <button
          type="submit"
          disabled={isPending}
          className="w-full bg-indigo-600 hover:bg-indigo-700 disabled:bg-indigo-400 text-white font-semibold rounded-lg py-2.5 text-sm transition-colors"
        >
          {isPending ? "Creating account..." : "Create account"}
        </button>
      </form>

      <p className="text-center text-sm text-gray-500 mt-6">
        Already have an account?{" "}
        <Link to="/login" className="text-indigo-600 font-medium hover:underline">
          Sign in
        </Link>
      </p>
    </AuthLayout>
  );
};

export default Signup;