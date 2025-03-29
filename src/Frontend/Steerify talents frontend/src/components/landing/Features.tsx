
import { Button } from "@/components/ui/button";
import { Check } from "lucide-react";
import { Card } from "@/components/ui/card";
import { motion } from "framer-motion";
import { Link } from "react-router-dom";

const Features = () => {
  const features = [
    {
      title: "For Clients",
      description: "Find and manage top talent for your creative projects with our comprehensive platform.",
      points: ["Access a global pool of vetted professionals", "Streamlined hiring and contracting", "Project management and collaboration tools"],
      buttonText: "Sign up as Client",
      buttonLink: "/client-signup",
      loginLink: "/client-login",
      imgSrc: "/placeholder.svg",
      altText: "Client dashboard preview"
    },
    {
      title: "For Talents",
      description: "Showcase your skills, find exciting opportunities, and grow your career with Steerify Talents.",
      points: ["Create a professional digital portfolio", "Get discovered by top employers", "Manage projects and payments in one place"],
      buttonText: "Sign up as Talent",
      buttonLink: "/talent-signup",
      loginLink: "/talent-login",
      imgSrc: "/placeholder.svg",
      altText: "Talent profile preview"
    }
  ];

  return (
    <section className="py-24" id="features">
      <div className="container-padding">
        <div className="text-center mb-16">
          <span className="bg-accent-purple/10 text-accent-purple px-4 py-1.5 rounded-full text-sm font-medium">
            JOIN STEERIFY TALENTS
          </span>
          <h2 className="heading-lg mt-6">Find Your Perfect Match</h2>
          <p className="text-neutral-600 mt-4 max-w-2xl mx-auto">
            Whether you're looking for talent or showcasing your skills, Steerify Talents has you covered.
          </p>
        </div>

        {features.map((feature, index) => (
          <motion.div 
            key={feature.title} 
            className={`flex flex-col md:flex-row gap-12 items-center mb-24 ${index % 2 === 1 ? 'md:flex-row-reverse' : ''}`}
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: index * 0.2 }}
            viewport={{ once: true }}
          >
            <div className="flex-1">
              <h3 className="text-2xl font-bold mb-4">{feature.title}</h3>
              <p className="text-neutral-600 mb-6">{feature.description}</p>
              <ul className="space-y-4">
                {feature.points.map((point) => (
                  <li key={point} className="flex items-center gap-3">
                    <div className="w-5 h-5 rounded-full bg-accent-purple/10 flex items-center justify-center">
                      <Check className="w-3 h-3 text-accent-purple" />
                    </div>
                    {point}
                  </li>
                ))}
              </ul>
              <div className="mt-8 flex gap-4">
                <Link to={feature.buttonLink}>
                  <Button className="px-6 py-6 font-medium bg-primary hover:bg-primary/90 text-white">
                    {feature.buttonText}
                  </Button>
                </Link>
                <Link to={feature.loginLink}>
                  <Button variant="outline" className="px-6 py-6 border-neutral-300 text-neutral-600 hover:bg-neutral-100">
                    Log in instead
                  </Button>
                </Link>
              </div>
            </div>
            <div className="flex-1">
              <Card className="glass-panel p-6 rounded-2xl overflow-hidden transform transition-all duration-300 hover:shadow-xl hover:-translate-y-1">
                <img 
                  src={feature.imgSrc} 
                  alt={feature.altText}
                  className="w-full h-auto rounded-lg"
                />
              </Card>
            </div>
          </motion.div>
        ))}
      </div>
    </section>
  );
};

export default Features;
