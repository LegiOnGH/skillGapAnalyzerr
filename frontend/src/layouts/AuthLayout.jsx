const AuthLayout = ({ children }) => {
  return (
    <div className="min-h-screen bg-gray-900 flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        {/* logo / brand */}
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-white">SkillBridge</h1>
          <p className="text-gray-400 mt-2">Gap Analysis and Repo Discovery</p>
        </div>

        {/* card */}
        <div className="bg-white rounded-2xl shadow-xl p-8">
          {children}
        </div>
      </div>
    </div>
  );
};

export default AuthLayout;