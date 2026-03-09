import { useNavigate } from "react-router-dom";

const Home = () => {

  const navigate = useNavigate();

  return (
    <div className="relative min-h-screen text-white">
      {/* BACKGROUND IMAGE */}
      <div
        className="absolute inset-0 bg-cover bg-center"
        style={{
          backgroundImage: `url('/hero.png')`,
        }}
      />

      {/* DARK OVERLAY */}
      <div className="absolute inset-0 bg-black/80" />

      {/* Purple tint top-left */}
      <div className="absolute inset-0 bg-[radial-gradient(circle_at_top_left,rgba(88,28,135,0.45),transparent_55%)]" />

      {/* Purple tint bottom-right */}
      <div className="absolute inset-0 bg-[radial-gradient(circle_at_bottom_right,rgba(88,28,135,0.35),transparent_50%)]" />

      {/* CONTENT WRAPPER */}
      <div className="relative z-10">
        {/* NAVBAR */}
        <nav className="flex justify-between items-center px-12 py-6 max-w-7xl mx-auto">
          <div className="text-2xl font-bold tracking-wide">
            Jobsy
          </div>

          <div className="hidden md:flex gap-12 text-[15px] font-medium">
            <a className="hover:text-purple-400 cursor-pointer">Product</a>
            <a className="hover:text-purple-400 cursor-pointer">Features</a>
            <a className="hover:text-purple-400 cursor-pointer">Marketplace</a>
            <a className="hover:text-purple-400 cursor-pointer">Company</a>
          </div>

          <button onClick={() => navigate("/login")} className="text-sm hover:text-purple-400">Log in →</button>
        </nav>

        {/* HERO */}
        <div className="max-w-5xl mx-auto text-center pt-24 px-6">
          {/* Announcement pill */}
          <div
            className="
            inline-flex items-center gap-2
            border border-gray-700/70
            rounded-full px-5 py-1.5
            text-sm mb-8
            backdrop-blur-sm
          "
          >
            <span className="text-gray-300">
              Trusted platform for freelancers and clients worldwide.
            </span>
          </div>

          {/* Title */}
          <h1
            className="
            text-[52px] md:text-[64px]
            font-semibold
            leading-[1.1]
            tracking-tight
          "
          >
            Become a Freelancer or <br />
            Hire Top Talent on Jobsy
          </h1>

          {/* Subtitle */}
          <p className="text-gray-400 mt-6 text-lg max-w-2xl mx-auto">
            Jobsy connects talented freelancers with clients worldwide. 
            Find high-quality projects, hire skilled professionals, and collaborate seamlessly. 
            Whether you're looking to earn, build your career, or grow your business — Jobsy makes it simple, fast, and secure.
          </p>

          {/* Buttons */}
          <div className="flex items-center justify-center gap-6 mt-10">
            <button
              onClick={() => navigate("/login")}
              className="
              bg-purple-600 hover:bg-purple-700
              transition
              px-7 py-3.5 rounded-lg
              font-medium text-[15px]
            "
            >
              Get started
            </button>

            <button
              className="
              flex items-center gap-1
              hover:text-purple-400 text-[15px]
            "
            >
              Learn more →
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;
