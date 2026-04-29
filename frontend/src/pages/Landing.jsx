import { Link } from "react-router-dom";

const features = [
  {
    icon: "◎",
    title: "Skill Gap Analysis",
    description:
      "Compare your current skills against any target role and instantly see what's missing.",
  },
  {
    icon: "☰",
    title: "Track Your Progress",
    description:
      "Save every analysis and track how your skill match percentage improves over time.",
  },
  {
    icon: "⌥",
    title: "GitHub Repo Finder",
    description:
      "Get curated GitHub repositories to practice the exact skills you're missing.",
  },
  {
    icon: "❏",
    title: "Learning Resources",
    description:
      "Access hand-picked learning resources for every skill gap identified in your analysis.",
  },
];

const steps = [
  { number: "01", title: "Choose a Role", description: "Pick your target job role from our curated list." },
  { number: "02", title: "Add Your Skills", description: "Select the skills you already have." },
  { number: "03", title: "Get Your Analysis", description: "See your match score, gaps, and resources instantly." },
];

const Landing = () => {
  return (
    <div className="min-h-screen bg-gray-950 text-white">

      {/* navbar */}
      <nav className="flex items-center justify-between px-8 py-5 border-b border-gray-800">
        <div>
          <span className="text-xl font-bold text-white">SkillBridge</span>
          <span className="text-indigo-400 font-bold text-xl">.</span>
        </div>
        <div className="flex items-center gap-4">
          <Link
            to="/login"
            className="text-sm text-gray-400 hover:text-white transition-colors"
          >
            Sign in
          </Link>
          <Link
            to="/signup"
            className="text-sm bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg transition-colors"
          >
            Get started
          </Link>
        </div>
      </nav>

      {/* hero */}
      <section className="max-w-4xl mx-auto text-center px-8 py-24">
        <div className="inline-block bg-indigo-900 text-indigo-300 text-xs font-semibold px-3 py-1 rounded-full mb-6 mt-2 uppercase tracking-wider">
          Free for students
        </div>
        <h1 className="text-5xl font-extrabold leading-tight mb-6 mt-2">
          Know exactly what skills
          <br />
          <span className="text-indigo-400">you need to get hired.</span>
        </h1>
        <p className="text-gray-400 text-lg mb-10 max-w-2xl mx-auto">
          SkillGap analyzes your current skills against any target role, shows
          you what's missing, and points you to the right resources to close
          the gap.
        </p>
        <div className="flex items-center justify-center gap-4">
          <Link
            to="/signup"
            className="bg-indigo-600 hover:bg-indigo-700 text-white font-semibold px-6 py-3 rounded-lg transition-colors"
          >
            Analyze your skills →
          </Link>
          <Link
            to="/login"
            className="text-gray-400 hover:text-white font-medium px-6 py-3 transition-colors"
          >
            Sign in
          </Link>
        </div>
      </section>

      {/* how it works */}
      <section className="max-w-4xl mx-auto px-8 py-16">
        <h2 className="text-2xl font-bold text-center mb-12">How it works</h2>
        <div className="grid grid-cols-1 gap-8 md:grid-cols-3">
          {steps.map((step) => (
            <div key={step.number} className="text-center">
              <div className="text-4xl font-extrabold text-indigo-500 mb-3">
                {step.number}
              </div>
              <h3 className="text-lg font-semibold mb-2">{step.title}</h3>
              <p className="text-gray-400 text-sm">{step.description}</p>
            </div>
          ))}
        </div>
      </section>

      {/* features */}
      <section className="max-w-5xl mx-auto px-8 py-16">
        <h2 className="text-2xl font-bold text-center mb-12">Everything you need</h2>
        <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
          {features.map((feature) => (
            <div
              key={feature.title}
              className="bg-gray-900 border border-gray-800 rounded-xl p-6 hover:border-indigo-500 transition-colors"
            >
              <div className="text-2xl mb-3">{feature.icon}</div>
              <h3 className="text-lg font-semibold mb-2">{feature.title}</h3>
              <p className="text-gray-400 text-sm">{feature.description}</p>
            </div>
          ))}
        </div>
      </section>

      {/* cta banner */}
      <section className="max-w-4xl mx-auto px-8 py-16">
        <div className="bg-indigo-600 rounded-2xl p-12 text-center">
          <h2 className="text-3xl font-bold mb-4">Ready to close your skill gap?</h2>
          <p className="text-indigo-200 mb-8">
            Join and start analyzing your skills against your dream role today.
          </p>
          <Link
            to="/signup"
            className="bg-white text-indigo-600 font-semibold px-8 py-3 rounded-lg hover:bg-gray-100 transition-colors"
          >
            Get started for free
          </Link>
        </div>
      </section>

      {/* footer */}
      <footer className="border-t border-gray-800 px-8 py-6 text-center text-gray-500 text-sm">
        © 2025 SkillBridge. Built with Spring Boot + React.
      </footer>
    </div>
  );
};

export default Landing;